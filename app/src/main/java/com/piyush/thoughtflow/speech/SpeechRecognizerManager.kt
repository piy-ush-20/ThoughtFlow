package com.piyush.thoughtflow.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log

class SpeechRecognizerManager(
    private val context: Context
) {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val _transcript = MutableStateFlow("")

    val transcript: StateFlow<String> = _transcript.asStateFlow()

    init {
        recognizer.setRecognitionListener(
            object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val text =
                        results
                            ?.getStringArrayList(
                                SpeechRecognizer.RESULTS_RECOGNITION
                            )
                            ?.firstOrNull()
                    _transcript.value = text.orEmpty()
                }

                override fun onError(error: Int) {
                    Log.e("Speech", "Error: $error")
                }

                override fun onReadyForSpeech(param: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            }
        )
    }

    fun startListening() {
        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
            )
        }
        recognizer.startListening(intent)
    }

    fun stopListening() {
        recognizer.stopListening()
    }

    fun destroy() {
        recognizer.destroy()
    }
}