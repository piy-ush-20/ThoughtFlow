package com.piyush.thoughtflow.domain.session

import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.Transcript
import com.piyush.thoughtflow.domain.model.VoiceSessionState

/**
 * Pure state machine for the voice-to-document session.
 */
class VoiceSessionStateMachine {

    fun transition(current: VoiceSessionState, event: VoiceSessionEvent): VoiceSessionState {
        return when (event) {
            VoiceSessionEvent.StartListening -> when (current) {
                is VoiceSessionState.Idle,
                is VoiceSessionState.Saved,
                is VoiceSessionState.Error,
                -> VoiceSessionState.Listening()
                else -> current
            }

            is VoiceSessionEvent.TranscriptUpdated -> when (current) {
                is VoiceSessionState.Listening -> {
                    if (event.transcript.partialText.isNotBlank() || event.transcript.finalText.isNotBlank()) {
                        VoiceSessionState.Transcribing(event.transcript, event.audioLevel)
                    } else {
                        current.copy(transcript = event.transcript, audioLevel = event.audioLevel)
                    }
                }
                is VoiceSessionState.Transcribing -> current.copy(
                    transcript = event.transcript,
                    audioLevel = event.audioLevel,
                )
                else -> current
            }

            VoiceSessionEvent.FinishListening -> when (current) {
                is VoiceSessionState.Listening -> VoiceSessionState.Formatting(current.transcript)
                is VoiceSessionState.Transcribing -> VoiceSessionState.Formatting(current.transcript)
                else -> current
            }

            VoiceSessionEvent.StartFormatting -> when (current) {
                is VoiceSessionState.Formatting -> current
                is VoiceSessionState.Listening -> VoiceSessionState.Formatting(current.transcript)
                is VoiceSessionState.Transcribing -> VoiceSessionState.Formatting(current.transcript)
                else -> current
            }

            is VoiceSessionEvent.FormattingComplete -> VoiceSessionState.Editing(event.documentId)

            is VoiceSessionEvent.Saved -> VoiceSessionState.Saved(event.documentId)

            VoiceSessionEvent.Reset -> VoiceSessionState.Idle

            is VoiceSessionEvent.Fail -> VoiceSessionState.Error(
                message = event.message,
                recoverable = event.recoverable,
                previous = current,
            )

            VoiceSessionEvent.DismissError -> when (current) {
                is VoiceSessionState.Error -> current.previous ?: VoiceSessionState.Idle
                else -> current
            }
        }
    }
}

sealed class VoiceSessionEvent {
    data object StartListening : VoiceSessionEvent()
    data class TranscriptUpdated(
        val transcript: Transcript,
        val audioLevel: Float = 0f,
    ) : VoiceSessionEvent()
    data object FinishListening : VoiceSessionEvent()
    data object StartFormatting : VoiceSessionEvent()
    data class FormattingComplete(val documentId: DocumentId) : VoiceSessionEvent()
    data class Saved(val documentId: DocumentId) : VoiceSessionEvent()
    data object Reset : VoiceSessionEvent()
    data class Fail(val message: String, val recoverable: Boolean = true) : VoiceSessionEvent()
    data object DismissError : VoiceSessionEvent()
}
