package com.piyush.thoughtflow.ai

import android.util.Log
import com.piyush.thoughtflow.domain.model.FormattedDocument
import javax.inject.Inject
import javax.inject.Singleton

/**
 * On-device Gemini Nano via Android AI Core when present on the device.
 * Reports unavailable when the runtime / feature is missing so Adaptive can fall through.
 */
@Singleton
class GeminiNanoFormatter @Inject constructor() : FormatterStrategy {

    override val id: String = ID

    override suspend fun isAvailable(): Boolean {
        return runCatching {
            val clazz = Class.forName("com.google.mlkit.genai.prompt.Generation")
            clazz != null && probeFeature()
        }.getOrDefault(false)
    }

    private fun probeFeature(): Boolean {
        // Without the optional AI Core dependency on the classpath this stays false.
        // When AI Core is added later, replace with real feature-status checks.
        return false
    }

    override suspend fun format(text: String): FormattedDocument {
        check(isAvailable()) { "Gemini Nano is not available" }
        Log.d(TAG, "Formatting with Gemini Nano (${text.length} chars)")
        // Placeholder for AI Core prompt API integration.
        throw UnsupportedOperationException("Gemini Nano runtime not linked in this build")
    }

    companion object {
        const val ID = "gemini-nano"
        private const val TAG = "GeminiNanoFormatter"
    }
}
