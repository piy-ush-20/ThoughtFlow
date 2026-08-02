package com.piyush.thoughtflow.processing

import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.Transcript
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.domain.repository.SpeechRepository
import com.piyush.thoughtflow.domain.session.VoiceSessionEvent
import com.piyush.thoughtflow.domain.session.VoiceSessionStateMachine
import com.piyush.thoughtflow.domain.usecase.FormatTranscriptUseCase
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.StartListeningUseCase
import com.piyush.thoughtflow.domain.usecase.StopListeningUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates Speak → STT → AI → Saved draft document.
 * Ensures speech is stopped and transcripts cleared of session buffers after finish.
 */
@Singleton
class VoiceDocumentPipeline @Inject constructor(
    private val speechRepository: SpeechRepository,
    private val startListening: StartListeningUseCase,
    private val stopListening: StopListeningUseCase,
    private val formatTranscript: FormatTranscriptUseCase,
    private val saveDocument: SaveDocumentUseCase,
) {
    private val machine = VoiceSessionStateMachine()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow<VoiceSessionState>(VoiceSessionState.Idle)
    val state: StateFlow<VoiceSessionState> = _state.asStateFlow()

    private var observeJob: Job? = null
    private var formatJob: Job? = null

    fun startSession() {
        if (!speechRepository.isAvailable()) {
            reduce(VoiceSessionEvent.Fail("Speech recognition is not available on this device"))
            return
        }
        formatJob?.cancel()
        observeJob?.cancel()
        reduce(VoiceSessionEvent.StartListening)
        startListening()
        observeJob = scope.launch {
            combine(
                speechRepository.transcript,
                speechRepository.audioLevel,
            ) { transcript, level -> transcript to level }
                .collect { (transcript, level) ->
                    val current = _state.value
                    if (current is VoiceSessionState.Listening || current is VoiceSessionState.Transcribing) {
                        reduce(VoiceSessionEvent.TranscriptUpdated(transcript, level))
                    }
                }
        }
        scope.launch {
            speechRepository.errors.collect { error ->
                val current = _state.value
                if (current is VoiceSessionState.Listening || current is VoiceSessionState.Transcribing) {
                    // Soft errors are handled by engine restart; surface permission hard failures
                    if (error.message.contains("permission", ignoreCase = true) ||
                        error.message.contains("not available", ignoreCase = true)
                    ) {
                        stopListening()
                        reduce(VoiceSessionEvent.Fail(error.message))
                    }
                }
            }
        }
    }

    fun finishSession() {
        observeJob?.cancel()
        observeJob = null
        stopListening()
        // Brief delay so final results can flush
        formatJob?.cancel()
        formatJob = scope.launch {
            delay(350)
            val transcript = speechRepository.transcript.value
            val text = transcript.committedText
            reduce(VoiceSessionEvent.FinishListening)
            if (text.isBlank()) {
                cleanupSpeech()
                reduce(VoiceSessionEvent.Fail("No speech detected. Hold the mic and try again."))
                reduce(VoiceSessionEvent.Reset)
                return@launch
            }
            reduce(VoiceSessionEvent.TranscriptUpdated(transcript.copy(partialText = "", finalText = text)))
            try {
                val formatted = formatTranscript(text)
                val document = saveDocument(formatted)
                cleanupSpeech()
                reduce(VoiceSessionEvent.FormattingComplete(document.id))
            } catch (t: Throwable) {
                cleanupSpeech()
                reduce(VoiceSessionEvent.Fail(t.message ?: "Failed to format document"))
            }
        }
    }

    fun cancelSession() {
        formatJob?.cancel()
        observeJob?.cancel()
        speechRepository.cancel()
        cleanupSpeech()
        reduce(VoiceSessionEvent.Reset)
    }

    fun dismissError() {
        reduce(VoiceSessionEvent.DismissError)
        if (_state.value is VoiceSessionState.Error) {
            reduce(VoiceSessionEvent.Reset)
        }
    }

    fun markSaved(documentId: DocumentId) {
        reduce(VoiceSessionEvent.Saved(documentId))
    }

    fun reset() {
        reduce(VoiceSessionEvent.Reset)
    }

    fun currentTranscript(): Transcript = when (val s = _state.value) {
        is VoiceSessionState.Listening -> s.transcript
        is VoiceSessionState.Transcribing -> s.transcript
        is VoiceSessionState.Formatting -> s.transcript
        else -> speechRepository.transcript.value
    }

    fun destroy() {
        formatJob?.cancel()
        observeJob?.cancel()
        cleanupSpeech()
        speechRepository.destroy()
        scope.cancel()
    }

    private fun cleanupSpeech() {
        speechRepository.clearTranscript()
        // Privacy: no temp audio files are written by the platform STT path.
    }

    private fun reduce(event: VoiceSessionEvent) {
        _state.value = machine.transition(_state.value, event)
    }
}
