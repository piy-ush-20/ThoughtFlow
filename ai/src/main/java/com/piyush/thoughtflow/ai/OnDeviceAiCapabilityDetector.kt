package com.piyush.thoughtflow.ai

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.speech.SpeechRecognizer
import android.util.Log
import com.piyush.thoughtflow.domain.model.OnDeviceAiCapabilities
import com.piyush.thoughtflow.domain.model.OnDeviceFeatureStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Detects on-device AI / speech capabilities without hard-depending on ML Kit GenAI.
 *
 * Strategy:
 * 1. Probe AICore package presence and stub vs real install.
 * 2. Reflectively call ML Kit Prompt Generation.getClient().checkStatus when present.
 * 3. Report on-device speech recognition availability (API 31+).
 */
@Singleton
class OnDeviceAiCapabilityDetector (
    private val probes: DeviceAiProbes,
) {
    @Inject
    constructor(@ApplicationContext context: Context) : this(AndroidDeviceAiProbes(context))

    suspend fun detect(): OnDeviceAiCapabilities = withContext(Dispatchers.IO) {
        val versionName = probes.aiCoreVersionName()
        val installed = versionName != null || probes.isAiCoreInstalled()
        val isStub = versionName?.startsWith(AICORE_STUB_PREFIX, ignoreCase = true) == true
        val sdkPresent = probes.isGenAiSdkPresent()
        val speech = probes.isOnDeviceSpeechAvailable()

        val geminiStatus = when {
            !sdkPresent -> OnDeviceFeatureStatus.SdkMissing
            else -> probes.checkGeminiNanoFeatureStatus()
        }

        val capabilities = OnDeviceAiCapabilities(
            aiCoreInstalled = installed,
            aiCoreIsStub = isStub,
            aiCoreVersionName = versionName,
            genAiSdkPresent = sdkPresent,
            geminiNanoStatus = geminiStatus,
            onDeviceSpeechAvailable = speech,
            heuristicAvailable = true,
        )
        Log.i(TAG, "On-device AI capabilities: $capabilities (${capabilities.summaryLabel()})")
        capabilities
    }

    companion object {
        private const val TAG = "OnDeviceAiCaps"
        const val AICORE_PACKAGE = "com.google.android.aicore"
        const val AICORE_STUB_PREFIX = "0.stub"
        const val GENERATION_CLASS = "com.google.mlkit.genai.prompt.Generation"
        const val FEATURE_STATUS_CLASS = "com.google.mlkit.genai.common.FeatureStatus"
    }
}

/**
 * Platform seams so unit tests can drive detection without Android runtime.
 */
interface DeviceAiProbes {
    fun isAiCoreInstalled(): Boolean
    fun aiCoreVersionName(): String?
    fun isGenAiSdkPresent(): Boolean
    fun checkGeminiNanoFeatureStatus(): OnDeviceFeatureStatus
    fun isOnDeviceSpeechAvailable(): Boolean
}

class AndroidDeviceAiProbes(
    private val context: Context,
) : DeviceAiProbes {

    override fun isAiCoreInstalled(): Boolean =
        runCatching {
            context.packageManager.getPackageInfoCompat(OnDeviceAiCapabilityDetector.AICORE_PACKAGE)
            true
        }.getOrDefault(false)

    override fun aiCoreVersionName(): String? =
        runCatching {
            context.packageManager
                .getPackageInfoCompat(OnDeviceAiCapabilityDetector.AICORE_PACKAGE)
                .versionName
        }.getOrNull()

    override fun isGenAiSdkPresent(): Boolean =
        runCatching {
            Class.forName(OnDeviceAiCapabilityDetector.GENERATION_CLASS)
            true
        }.getOrDefault(false)

    override fun checkGeminiNanoFeatureStatus(): OnDeviceFeatureStatus {
        return runCatching {
            val generationClass = Class.forName(OnDeviceAiCapabilityDetector.GENERATION_CLASS)
            val client = generationClass.getMethod("getClient").invoke(null)
                ?: return OnDeviceFeatureStatus.Unavailable
            val raw = invokeCheckStatus(client) ?: return OnDeviceFeatureStatus.Unavailable
            FeatureStatusMapper.map(raw)
        }.onFailure { e ->
            Log.w("OnDeviceAiCaps", "Gemini Nano status probe failed: ${e.message}")
        }.getOrDefault(OnDeviceFeatureStatus.Unavailable)
    }

    override fun isOnDeviceSpeechAvailable(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return false
        return runCatching {
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
        }.getOrDefault(false)
    }

    private fun invokeCheckStatus(client: Any): Any? {
        val method = client.javaClass.methods.firstOrNull {
            it.name == "checkStatus" && it.parameterCount == 0
        } ?: return null
        val result = method.invoke(client) ?: return null
        // ML Kit may return Int, enum, Task, or ListenableFuture depending on binding.
        return unwrapAsync(result) ?: result
    }

    private fun unwrapAsync(result: Any): Any? {
        runCatching {
            val tasksClass = Class.forName("com.google.android.gms.tasks.Tasks")
            val taskClass = Class.forName("com.google.android.gms.tasks.Task")
            if (taskClass.isInstance(result)) {
                return tasksClass
                    .getMethod(
                        "await",
                        taskClass,
                        Long::class.javaPrimitiveType,
                        TimeUnit::class.java,
                    )
                    .invoke(null, result, 5L, TimeUnit.SECONDS)
            }
        }
        runCatching {
            val futureClass = Class.forName("java.util.concurrent.Future")
            if (futureClass.isInstance(result)) {
                return futureClass
                    .getMethod("get", Long::class.javaPrimitiveType, TimeUnit::class.java)
                    .invoke(result, 5L, TimeUnit.SECONDS)
            }
        }
        return null
    }
}

/**
 * Maps ML Kit FeatureStatus (Int constants, enums, or names) to domain status.
 */
object FeatureStatusMapper {
    fun map(raw: Any): OnDeviceFeatureStatus {
        val name = when (raw) {
            is Enum<*> -> raw.name
            is Number -> featureStatusNameFromInt(raw.toInt())
            is String -> raw
            else -> raw.toString()
        }.uppercase()

        return when {
            name.contains("UNAVAILABLE") -> OnDeviceFeatureStatus.Unavailable
            name.contains("DOWNLOADABLE") -> OnDeviceFeatureStatus.Downloadable
            name.contains("DOWNLOADING") -> OnDeviceFeatureStatus.Downloading
            name.contains("AVAILABLE") -> OnDeviceFeatureStatus.Available
            else -> when ((raw as? Number)?.toInt()) {
                3 -> OnDeviceFeatureStatus.Available
                2 -> OnDeviceFeatureStatus.Downloading
                1 -> OnDeviceFeatureStatus.Downloadable
                0 -> OnDeviceFeatureStatus.Unavailable
                else -> OnDeviceFeatureStatus.Unavailable
            }
        }
    }

    private fun featureStatusNameFromInt(value: Int): String {
        return runCatching {
            val clazz = Class.forName(OnDeviceAiCapabilityDetector.FEATURE_STATUS_CLASS)
            clazz.fields
                .firstOrNull { field ->
                    field.type == Int::class.javaPrimitiveType &&
                        runCatching { field.getInt(null) == value }.getOrDefault(false)
                }
                ?.name
        }.getOrNull() ?: value.toString()
    }
}

@Suppress("DEPRECATION")
private fun PackageManager.getPackageInfoCompat(packageName: String) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        getPackageInfo(packageName, 0)
    }
