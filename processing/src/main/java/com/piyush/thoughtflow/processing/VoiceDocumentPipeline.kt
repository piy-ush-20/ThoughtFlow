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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orchestrates Speak → STT → AI → Saved draft document.
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
    private var errorsJob: Job? = null
    private var formatJob: Job? = null

    fun startSession() {
        if (!speechRepository.isAvailable()) {
            reduce(VoiceSessionEvent.Fail("Speech recognition is not available on this device"))
            return
        }
        formatJob?.cancel()
        observeJob?.cancel()
        errorsJob?.cancel()
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
        errorsJob = scope.launch {
            speechRepository.errors.collect { error ->
                val current = _state.value
                if (current is VoiceSessionState.Listening || current is VoiceSessionState.Transcribing) {
                    if (error.message.contains("permission", ignoreCase = true) ||
                        error.message.contains("not available", ignoreCase = true) ||
                        error.message.contains("unavailable", ignoreCase = true)
                    ) {
                        stopListening()
                        reduce(VoiceSessionEvent.Fail(error.message))
                    }
                }
            }
        }
    }

    fun finishSession() {
        errorsJob?.cancel()
        errorsJob = null
        stopListening()
        formatJob?.cancel()
        formatJob = scope.launch {
            // Wait for the engine to flush final partials / onResults after stopListening().
            val text = awaitFinalTranscript()
            observeJob?.cancel()
            observeJob = null
            reduce(VoiceSessionEvent.FinishListening)
            if (text.isBlank()) {
                cleanupSpeech()
                reduce(
                    VoiceSessionEvent.Fail(
                        "No speech detected. Tap the mic, speak clearly, then tap again to finish.",
                    ),
                )
                reduce(VoiceSessionEvent.Reset)
                return@launch
            }
            reduce(
                VoiceSessionEvent.TranscriptUpdated(
                    Transcript(partialText = "", finalText = text),
                ),
            )
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

    private suspend fun awaitFinalTranscript(): String {
        // Prefer any non-blank committed text; keep polling while recognizer settles.
        withTimeoutOrNull(FINAL_WAIT_MS) {
            // First, wait until listening reports false (engine finished stop).
            runCatching {
                speechRepository.isListening.first { !it }
            }
        }
        // Extra settle time for late onResults.
        delay(250)
        var best = speechRepository.transcript.value.committedText
        if (best.isNotBlank()) return best.trim()

        withTimeoutOrNull(EXTRA_POLL_MS) {
            while (true) {
                delay(150)
                val current = speechRepository.transcript.value.committedText
                if (current.isNotBlank()) {
                    best = current
                    return@withTimeoutOrNull current
                }
            }
        }
        return best.trim()
    }

    fun cancelSession() {
        formatJob?.cancel()
        observeJob?.cancel()
        errorsJob?.cancel()
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
        errorsJob?.cancel()
        cleanupSpeech()
        speechRepository.destroy()
        scope.cancel()
    }

    private fun cleanupSpeech() {
        speechRepository.clearTranscript()
    }

    private fun reduce(event: VoiceSessionEvent) {
        _state.value = machine.transition(_state.value, event)
    }

    companion object {
        private const val FINAL_WAIT_MS = 3_000L
        private const val EXTRA_POLL_MS = 1_500L
    }
}
