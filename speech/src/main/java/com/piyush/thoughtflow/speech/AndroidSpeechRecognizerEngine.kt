package com.piyush.thoughtflow.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.piyush.thoughtflow.domain.model.SpeechError
import com.piyush.thoughtflow.domain.model.Transcript
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.ref.WeakReference
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

/**
 * Platform [SpeechRecognizer] engine.
 *
 * IMPORTANT: Many OEMs return ERROR_NO_MATCH / silent failure when the
 * recognizer is created with an Application context. Always call
 * [bindToActivity] from the Activity before starting.
 *
 * Build marker: v3-activity-context — if logcat still shows
 * "Speech error code=N sessionActive=", the device is running an old APK.
 */
@Singleton
class AndroidSpeechRecognizerEngine @Inject constructor(
    @param:ApplicationContext private val appContext: Context,
) : SpeechEngine {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var activityRef: WeakReference<Context> = WeakReference(null)
    private var recognizer: SpeechRecognizer? = null
    private var sessionActive = false
    private var stopping = false
    private var isStarting = false
    private var heardSpeech = false
    private var consecutiveFailures = 0
    private var committedFinal = StringBuilder()

    private val _transcript = MutableStateFlow(Transcript())
    override val transcript: StateFlow<Transcript> = _transcript.asStateFlow()

    private val _audioLevel = MutableStateFlow(0f)
    override val audioLevel: StateFlow<Float> = _audioLevel.asStateFlow()

    private val _errors = MutableSharedFlow<SpeechError>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val errors: SharedFlow<SpeechError> = _errors.asSharedFlow()

    private val _isListening = MutableStateFlow(false)
    override val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    override fun bindToActivity(activityContext: Context) {
        mainHandler.post {
            val previous = activityRef.get()
            activityRef = WeakReference(activityContext)
            Log.i(TAG, "bindToActivity context=${activityContext.javaClass.simpleName} build=$BUILD_MARKER")
            // Recreate recognizer against the Activity context.
            if (previous !== activityContext) {
                val wasListening = sessionActive && !stopping
                destroyRecognizer()
                if (wasListening) {
                    ensureRecognizer(forceRecreate = true)
                    beginListeningLocked()
                }
            }
        }
    }

    override fun unbindFromActivity() {
        mainHandler.post {
            Log.i(TAG, "unbindFromActivity")
            if (sessionActive || stopping) {
                sessionActive = false
                stopping = false
                cancelPendingWork()
                runCatching { recognizer?.cancel() }
            }
            destroyRecognizer()
            activityRef.clear()
            _isListening.value = false
            _audioLevel.value = 0f
        }
    }

    override fun isAvailable(): Boolean =
        SpeechRecognizer.isRecognitionAvailable(appContext)

    override fun startListening() {
        mainHandler.post {
            Log.i(TAG, "startListening build=$BUILD_MARKER hasActivity=${activityRef.get() != null}")
            if (!isAvailable()) {
                _errors.tryEmit(SpeechError.NotAvailable())
                return@post
            }
            if (activityRef.get() == null) {
                Log.e(TAG, "No Activity bound — SpeechRecognizer requires Activity context")
                _errors.tryEmit(
                    SpeechError.RecognitionFailed(
                        "Speech engine not ready. Open the app again and retry.",
                    ),
                )
                return@post
            }
            cancelPendingWork()
            sessionActive = true
            stopping = false
            consecutiveFailures = 0
            heardSpeech = false
            isStarting = false
            committedFinal = StringBuilder()
            _transcript.value = Transcript()
            _audioLevel.value = 0f
            ensureRecognizer(forceRecreate = true)
            beginListeningLocked()
        }
    }

    override fun stopListening() {
        mainHandler.post {
            Log.i(TAG, "stopListening sessionActive=$sessionActive stopping=$stopping")
            if (!sessionActive && !stopping) return@post
            cancelPendingRestarts()
            stopping = true
            sessionActive = false
            isStarting = false
            _audioLevel.value = 0f
            promotePartialToFinal()

            val active = recognizer
            if (active == null) {
                finishStop()
                return@post
            }
            runCatching { active.stopListening() }
            mainHandler.postDelayed({
                if (stopping) {
                    Log.w(TAG, "Stop timed out waiting for final callback")
                    finishStop()
                }
            }, TOKEN_STOP_TIMEOUT, STOP_TIMEOUT_MS)
        }
    }

    override fun cancel() {
        mainHandler.post {
            sessionActive = false
            stopping = false
            cancelPendingWork()
            isStarting = false
            _isListening.value = false
            _audioLevel.value = 0f
            runCatching { recognizer?.cancel() }
            destroyRecognizer()
        }
    }

    override fun clearTranscript() {
        committedFinal = StringBuilder()
        _transcript.value = Transcript()
    }

    override fun destroy() {
        mainHandler.post {
            sessionActive = false
            stopping = false
            cancelPendingWork()
            isStarting = false
            _isListening.value = false
            destroyRecognizer()
            activityRef.clear()
        }
    }

    private fun recognitionContext(): Context =
        activityRef.get() ?: appContext

    private fun finishStop() {
        cancelPendingWork()
        stopping = false
        _isListening.value = false
        _audioLevel.value = 0f
        promotePartialToFinal()
        mainHandler.postDelayed({
            if (!sessionActive && !stopping) {
                destroyRecognizer()
            }
        }, TOKEN_TEARDOWN, TEARDOWN_DELAY_MS)
    }

    private fun promotePartialToFinal() {
        _transcript.update { current ->
            val finalText = current.finalText.ifBlank {
                listOf(committedFinal.toString(), current.partialText)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                    .trim()
            }.ifBlank {
                committedFinal.toString().trim()
            }
            if (finalText.isNotBlank() && committedFinal.isEmpty()) {
                committedFinal.append(finalText)
            }
            Transcript(partialText = "", finalText = finalText)
        }
    }

    private fun ensureRecognizer(forceRecreate: Boolean = false) {
        if (forceRecreate) destroyRecognizer()
        if (recognizer != null) return
        val ctx = recognitionContext()
        Log.i(
            TAG,
            "Creating SpeechRecognizer with ${ctx.javaClass.simpleName} (activityBound=${activityRef.get() != null})",
        )
        recognizer = SpeechRecognizer.createSpeechRecognizer(ctx).also {
            it.setRecognitionListener(listener)
        }
    }

    private fun destroyRecognizer() {
        val current = recognizer ?: return
        recognizer = null
        runCatching { current.setRecognitionListener(noopListener) }
        runCatching { current.destroy() }
    }

    private fun beginListeningLocked() {
        if (!sessionActive || stopping || isStarting) return
        ensureRecognizer(forceRecreate = false)
        val engine = recognizer ?: return

        val locale = Locale.getDefault()
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, appContext.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, locale.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2_500L)
            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                2_500L,
            )
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1_200L)
        }
        isStarting = true
        heardSpeech = false
        _isListening.value = true
        Log.i(TAG, "startListening locked locale=${locale.toLanguageTag()}")
        runCatching {
            engine.startListening(intent)
        }.onFailure { e ->
            isStarting = false
            Log.e(TAG, "startListening failed", e)
            _isListening.value = false
            if (sessionActive) scheduleRestart(recreate = true)
        }
    }

    private fun scheduleRestart(recreate: Boolean) {
        if (!sessionActive || stopping) return
        cancelPendingRestarts()
        consecutiveFailures += 1
        if (consecutiveFailures > MAX_CONSECUTIVE_FAILURES) {
            Log.e(TAG, "Giving up after $consecutiveFailures speech failures")
            sessionActive = false
            _isListening.value = false
            destroyRecognizer()
            _errors.tryEmit(
                SpeechError.RecognitionFailed(
                    message = "Speech recognition unavailable. Ensure Google app / speech services are enabled.",
                    code = SpeechRecognizer.ERROR_CLIENT,
                ),
            )
            return
        }
        val delay = min(RESTART_BASE_DELAY_MS * consecutiveFailures, RESTART_MAX_DELAY_MS)
        mainHandler.postDelayed({
            if (!sessionActive || stopping) return@postDelayed
            if (recreate) ensureRecognizer(forceRecreate = true)
            beginListeningLocked()
        }, TOKEN_RESTART, delay)
    }

    private fun cancelPendingRestarts() {
        mainHandler.removeCallbacksAndMessages(TOKEN_RESTART)
    }

    private fun cancelPendingWork() {
        cancelPendingRestarts()
        mainHandler.removeCallbacksAndMessages(TOKEN_TEARDOWN)
        mainHandler.removeCallbacksAndMessages(TOKEN_STOP_TIMEOUT)
    }

    private fun appendFinal(text: String) {
        if (text.isBlank()) return
        if (committedFinal.isNotEmpty()) committedFinal.append(' ')
        committedFinal.append(text.trim())
        _transcript.value = Transcript(
            partialText = "",
            finalText = committedFinal.toString(),
        )
    }

    private fun updatePartial(partial: String) {
        val prefix = committedFinal.toString()
        val display = listOf(prefix, partial).filter { it.isNotBlank() }.joinToString(" ")
        _transcript.value = Transcript(
            partialText = display,
            finalText = prefix,
        )
    }

    private val noopListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) = Unit
        override fun onBeginningOfSpeech() = Unit
        override fun onRmsChanged(rmsdB: Float) = Unit
        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEndOfSpeech() = Unit
        override fun onError(error: Int) = Unit
        override fun onResults(results: Bundle?) = Unit
        override fun onPartialResults(partialResults: Bundle?) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            isStarting = false
            consecutiveFailures = 0
            Log.i(TAG, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            isStarting = false
            heardSpeech = true
            Log.i(TAG, "onBeginningOfSpeech")
        }

        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onRmsChanged(rmsdB: Float) {
            if (!sessionActive && !stopping) return
            val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
            _audioLevel.value = normalized.pow(0.85f)
        }

        override fun onPartialResults(partialResults: Bundle?) {
            if (!sessionActive && !stopping) return
            val partial = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            if (partial.isNotBlank()) {
                heardSpeech = true
                updatePartial(partial)
            }
        }

        override fun onResults(results: Bundle?) {
            isStarting = false
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            Log.i(TAG, "onResults text='${text.take(80)}' stopping=$stopping active=$sessionActive")
            if (text.isNotBlank()) appendFinal(text)
            when {
                stopping -> finishStop()
                sessionActive -> {
                    consecutiveFailures = 0
                    mainHandler.postDelayed({
                        if (sessionActive && !stopping) beginListeningLocked()
                    }, TOKEN_RESTART, RESTART_SUCCESS_DELAY_MS)
                }
                else -> {
                    _isListening.value = false
                    _audioLevel.value = 0f
                }
            }
        }

        override fun onEndOfSpeech() {
            isStarting = false
            Log.i(TAG, "onEndOfSpeech")
        }

        override fun onError(error: Int) {
            isStarting = false
            val label = errorLabel(error)
            // Unique format for build verification — old APKs log "Speech error code=".
            Log.w(
                TAG,
                "STT_ERROR build=$BUILD_MARKER code=$error ($label) active=$sessionActive stopping=$stopping heard=$heardSpeech",
            )

            if (stopping) {
                finishStop()
                return
            }
            if (!sessionActive) {
                _isListening.value = false
                _audioLevel.value = 0f
                return
            }

            when (error) {
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                    sessionActive = false
                    cancelPendingWork()
                    _isListening.value = false
                    destroyRecognizer()
                    _errors.tryEmit(SpeechError.PermissionDenied())
                }
                SpeechRecognizer.ERROR_NO_MATCH,
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
                -> scheduleRestart(recreate = false)
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY,
                SpeechRecognizer.ERROR_CLIENT,
                SpeechRecognizer.ERROR_SERVER_DISCONNECTED,
                SpeechRecognizer.ERROR_NETWORK,
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT,
                SpeechRecognizer.ERROR_SERVER,
                -> scheduleRestart(recreate = true)
                else -> scheduleRestart(recreate = true)
            }
        }
    }

    private fun errorLabel(code: Int): String = when (code) {
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "NETWORK_TIMEOUT"
        SpeechRecognizer.ERROR_NETWORK -> "NETWORK"
        SpeechRecognizer.ERROR_AUDIO -> "AUDIO"
        SpeechRecognizer.ERROR_SERVER -> "SERVER"
        SpeechRecognizer.ERROR_CLIENT -> "CLIENT"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "SPEECH_TIMEOUT"
        SpeechRecognizer.ERROR_NO_MATCH -> "NO_MATCH"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER_BUSY"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "INSUFFICIENT_PERMISSIONS"
        SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "TOO_MANY_REQUESTS"
        SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "SERVER_DISCONNECTED"
        SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "LANGUAGE_NOT_SUPPORTED"
        SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "LANGUAGE_UNAVAILABLE"
        else -> "UNKNOWN"
    }

    companion object {
        private const val TAG = "AndroidSpeechEngine"
        const val BUILD_MARKER = "v3-activity-context"
        private const val RESTART_SUCCESS_DELAY_MS = 300L
        private const val RESTART_BASE_DELAY_MS = 400L
        private const val RESTART_MAX_DELAY_MS = 2_000L
        private const val TEARDOWN_DELAY_MS = 300L
        private const val STOP_TIMEOUT_MS = 2_500L
        private const val MAX_CONSECUTIVE_FAILURES = 12
        private val TOKEN_RESTART = Any()
        private val TOKEN_TEARDOWN = Any()
        private val TOKEN_STOP_TIMEOUT = Any()
    }
}
