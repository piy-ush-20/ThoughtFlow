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
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

/**
 * Continuous speech recognition using the platform [SpeechRecognizer].
 *
 * Error 11 ([SpeechRecognizer.ERROR_SERVER_DISCONNECTED]) usually means the
 * recognition service binder died. Reusing that instance causes a tight restart
 * loop. We destroy + recreate before every restart, cancel pending restarts on
 * stop, and ignore teardown errors after the user session ends.
 */
@Singleton
class AndroidSpeechRecognizerEngine @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SpeechEngine {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var recognizer: SpeechRecognizer? = null
    private var sessionActive = false
    private var isStarting = false
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

    override fun isAvailable(): Boolean =
        SpeechRecognizer.isRecognitionAvailable(context)

    override fun startListening() {
        mainHandler.post {
            if (!isAvailable()) {
                _errors.tryEmit(SpeechError.NotAvailable())
                return@post
            }
            cancelPendingWork()
            sessionActive = true
            consecutiveFailures = 0
            isStarting = false
            committedFinal = StringBuilder()
            _transcript.value = Transcript()
            _audioLevel.value = 0f
            recreateRecognizer()
            beginListeningLocked()
        }
    }

    override fun stopListening() {
        mainHandler.post {
            // End session first so teardown callbacks are ignored.
            sessionActive = false
            cancelPendingWork()
            isStarting = false
            _isListening.value = false
            _audioLevel.value = 0f

            val active = recognizer
            runCatching { active?.stopListening() }

            // Destroy after a short delay so final results can still arrive.
            mainHandler.postDelayed({
                if (!sessionActive) {
                    destroyRecognizer()
                }
            }, TOKEN_TEARDOWN, TEARDOWN_DELAY_MS)

            _transcript.update { current ->
                val finalText = current.finalText.ifBlank {
                    listOf(committedFinal.toString(), current.partialText)
                        .filter { it.isNotBlank() }
                        .joinToString(" ")
                        .trim()
                }
                Transcript(partialText = "", finalText = finalText)
            }
        }
    }

    override fun cancel() {
        mainHandler.post {
            sessionActive = false
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
            cancelPendingWork()
            isStarting = false
            _isListening.value = false
            destroyRecognizer()
        }
    }

    private fun recreateRecognizer() {
        destroyRecognizer()
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).also {
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
        if (!sessionActive || isStarting) return
        val engine = recognizer ?: run {
            recreateRecognizer()
            recognizer
        } ?: return

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L)
            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                1000L,
            )
        }
        isStarting = true
        _isListening.value = true
        runCatching {
            engine.startListening(intent)
        }.onFailure { e ->
            isStarting = false
            Log.e(TAG, "startListening failed", e)
            _isListening.value = false
            if (sessionActive) {
                scheduleRestart()
            }
        }
    }

    private fun scheduleRestart() {
        if (!sessionActive) return
        cancelPendingRestarts()
        consecutiveFailures += 1
        if (consecutiveFailures > MAX_CONSECUTIVE_FAILURES) {
            Log.e(TAG, "Giving up after $consecutiveFailures speech failures")
            sessionActive = false
            _isListening.value = false
            destroyRecognizer()
            _errors.tryEmit(
                SpeechError.RecognitionFailed(
                    message = "Speech recognition unavailable. Check Google speech services and try again.",
                    code = SpeechRecognizer.ERROR_SERVER_DISCONNECTED,
                ),
            )
            return
        }
        val delay = min(
            RESTART_BASE_DELAY_MS * consecutiveFailures,
            RESTART_MAX_DELAY_MS,
        )
        mainHandler.postDelayed({
            if (!sessionActive) return@postDelayed
            recreateRecognizer()
            beginListeningLocked()
        }, TOKEN_RESTART, delay)
    }

    private fun scheduleSuccessfulRestart() {
        if (!sessionActive) return
        cancelPendingRestarts()
        consecutiveFailures = 0
        mainHandler.postDelayed({
            if (!sessionActive) return@postDelayed
            recreateRecognizer()
            beginListeningLocked()
        }, TOKEN_RESTART, RESTART_SUCCESS_DELAY_MS)
    }

    private fun cancelPendingRestarts() {
        mainHandler.removeCallbacksAndMessages(TOKEN_RESTART)
    }

    private fun cancelPendingWork() {
        cancelPendingRestarts()
        mainHandler.removeCallbacksAndMessages(TOKEN_TEARDOWN)
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
        }

        override fun onBeginningOfSpeech() {
            isStarting = false
        }

        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onRmsChanged(rmsdB: Float) {
            if (!sessionActive) return
            val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
            _audioLevel.value = normalized.pow(0.85f)
        }

        override fun onPartialResults(partialResults: Bundle?) {
            if (!sessionActive) return
            val partial = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            val prefix = committedFinal.toString()
            val display = listOf(prefix, partial).filter { it.isNotBlank() }.joinToString(" ")
            _transcript.value = Transcript(
                partialText = display,
                finalText = prefix,
            )
        }

        override fun onResults(results: Bundle?) {
            isStarting = false
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            if (text.isNotBlank()) {
                appendFinal(text)
            }
            if (sessionActive) {
                scheduleSuccessfulRestart()
            } else {
                _isListening.value = false
                _audioLevel.value = 0f
            }
        }

        override fun onEndOfSpeech() {
            isStarting = false
        }

        override fun onError(error: Int) {
            isStarting = false

            // Expected when the user releases the mic / we tear down the service.
            if (!sessionActive) {
                _isListening.value = false
                _audioLevel.value = 0f
                Log.d(TAG, "Ignoring speech error code=$error after session end")
                return
            }

            Log.w(TAG, "Speech error code=$error (${errorLabel(error)})")

            when (error) {
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                    sessionActive = false
                    cancelPendingWork()
                    _isListening.value = false
                    destroyRecognizer()
                    _errors.tryEmit(SpeechError.PermissionDenied())
                }
                else -> {
                    // ERROR_SERVER_DISCONNECTED (11), BUSY, NO_MATCH, TIMEOUT, NETWORK, etc.
                    // Always recreate the recognizer — reusing a disconnected binder loops forever.
                    scheduleRestart()
                }
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
        private const val RESTART_SUCCESS_DELAY_MS = 350L
        private const val RESTART_BASE_DELAY_MS = 500L
        private const val RESTART_MAX_DELAY_MS = 2_500L
        private const val TEARDOWN_DELAY_MS = 250L
        private const val MAX_CONSECUTIVE_FAILURES = 8
        private val TOKEN_RESTART = Any()
        private val TOKEN_TEARDOWN = Any()
    }
}
