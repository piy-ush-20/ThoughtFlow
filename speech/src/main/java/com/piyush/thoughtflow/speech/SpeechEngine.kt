package com.piyush.thoughtflow.speech

import android.content.Context
import com.piyush.thoughtflow.domain.model.SpeechError
import com.piyush.thoughtflow.domain.model.Transcript
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Provider-agnostic speech engine. Implementations must not retain audio after stop.
 *
 * [SpeechRecognizer] must be created with an Activity context on many OEMs —
 * call [bindToActivity] from [android.app.Activity.onResume].
 */
interface SpeechEngine {
    val transcript: StateFlow<Transcript>
    val audioLevel: StateFlow<Float>
    val errors: SharedFlow<SpeechError>
    val isListening: StateFlow<Boolean>

    fun bindToActivity(activityContext: Context)
    fun unbindFromActivity()

    fun isAvailable(): Boolean
    fun startListening()
    fun stopListening()
    fun cancel()
    fun clearTranscript()
    fun destroy()
}
