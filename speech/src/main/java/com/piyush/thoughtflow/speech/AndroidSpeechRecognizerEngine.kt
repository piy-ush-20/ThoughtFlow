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
import kotlin.math.pow

@Singleton
class AndroidSpeechRecognizerEngine @Inject constructor(
    @ApplicationContext private val context: Context,
) : SpeechEngine {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var recognizer: SpeechRecognizer? = null
    private var sessionActive = false
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
            sessionActive = true
            committedFinal = StringBuilder()
            _transcript.value = Transcript()
            _audioLevel.value = 0f
            ensureRecognizer()
            beginListeningLocked()
        }
    }

    override fun stopListening() {
        mainHandler.post {
            sessionActive = false
            _isListening.value = false
            _audioLevel.value = 0f
            runCatching { recognizer?.stopListening() }
            // Promote any leftover partial into final
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
            _isListening.value = false
            _audioLevel.value = 0f
            runCatching { recognizer?.cancel() }
        }
    }

    override fun clearTranscript() {
        committedFinal = StringBuilder()
        _transcript.value = Transcript()
    }

    override fun destroy() {
        mainHandler.post {
            sessionActive = false
            _isListening.value = false
            runCatching { recognizer?.destroy() }
            recognizer = null
        }
    }

    private fun ensureRecognizer() {
        if (recognizer != null) return
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).also {
            it.setRecognitionListener(listener)
        }
    }

    private fun beginListeningLocked() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }
        _isListening.value = true
        runCatching {
            recognizer?.startListening(intent)
        }.onFailure { e ->
            Log.e(TAG, "startListening failed", e)
            _errors.tryEmit(SpeechError.RecognitionFailed(e.message ?: "Failed to start listening"))
            _isListening.value = false
        }
    }

    private fun restartIfActive() {
        if (!sessionActive) return
        mainHandler.postDelayed({
            if (sessionActive) {
                beginListeningLocked()
            }
        }, RESTART_DELAY_MS)
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

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) = Unit
        override fun onBeginningOfSpeech() = Unit
        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onRmsChanged(rmsdB: Float) {
            // Map typical -2..10 dB-ish range into 0..1
            val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
            _audioLevel.value = normalized.pow(0.85f)
        }

        override fun onPartialResults(partialResults: Bundle?) {
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
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()
            appendFinal(text)
            if (sessionActive) {
                restartIfActive()
            } else {
                _isListening.value = false
                _audioLevel.value = 0f
            }
        }

        override fun onEndOfSpeech() {
            // Platform may call onResults next; keep session flag as-is
        }

        override fun onError(error: Int) {
            Log.w(TAG, "Speech error code=$error sessionActive=$sessionActive")
            when (error) {
                SpeechRecognizer.ERROR_CLIENT,
                SpeechRecognizer.ERROR_NO_MATCH,
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
                -> {
                    if (sessionActive) {
                        restartIfActive()
                    } else {
                        _isListening.value = false
                        _audioLevel.value = 0f
                    }
                }
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                    sessionActive = false
                    _isListening.value = false
                    _errors.tryEmit(SpeechError.PermissionDenied())
                }
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                    if (sessionActive) restartIfActive()
                }
                else -> {
                    if (sessionActive) {
                        restartIfActive()
                    } else {
                        _isListening.value = false
                        _errors.tryEmit(
                            SpeechError.RecognitionFailed(
                                message = "Speech recognition error ($error)",
                                code = error,
                            ),
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "AndroidSpeechEngine"
        private const val RESTART_DELAY_MS = 250L
    }
}
