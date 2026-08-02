package com.piyush.thoughtflow.speech

import com.piyush.thoughtflow.domain.model.SpeechError
import com.piyush.thoughtflow.domain.model.Transcript
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Provider-agnostic speech engine. Implementations must not retain audio after stop.
 */
interface SpeechEngine {
    val transcript: StateFlow<Transcript>
    val audioLevel: StateFlow<Float>
    val errors: SharedFlow<SpeechError>
    val isListening: StateFlow<Boolean>

    fun isAvailable(): Boolean
    fun startListening()
    fun stopListening()
    fun cancel()
    fun clearTranscript()
    fun destroy()
}
