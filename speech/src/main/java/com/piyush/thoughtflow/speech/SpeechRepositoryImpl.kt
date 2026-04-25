package com.piyush.thoughtflow.speech

import com.piyush.thoughtflow.domain.model.SpeechError
import com.piyush.thoughtflow.domain.model.Transcript
import com.piyush.thoughtflow.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRepositoryImpl @Inject constructor(
    private val engine: SpeechEngine,
) : SpeechRepository {
    override val transcript: StateFlow<Transcript> = engine.transcript
    override val audioLevel: StateFlow<Float> = engine.audioLevel
    override val errors: SharedFlow<SpeechError> = engine.errors
    override val isListening: StateFlow<Boolean> = engine.isListening

    override fun isAvailable(): Boolean = engine.isAvailable()
    override fun startListening() = engine.startListening()
    override fun stopListening() = engine.stopListening()
    override fun cancel() = engine.cancel()
    override fun clearTranscript() = engine.clearTranscript()
    override fun destroy() = engine.destroy()
}
