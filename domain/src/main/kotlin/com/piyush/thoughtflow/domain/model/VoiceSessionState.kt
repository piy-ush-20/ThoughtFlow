package com.piyush.thoughtflow.domain.model

sealed class VoiceSessionState {
    data object Idle : VoiceSessionState()

    data class Listening(
        val transcript: Transcript = Transcript(),
        val audioLevel: Float = 0f,
    ) : VoiceSessionState()

    data class Transcribing(
        val transcript: Transcript,
        val audioLevel: Float = 0f,
    ) : VoiceSessionState()

    data class Formatting(
        val transcript: Transcript,
    ) : VoiceSessionState()

    data class Editing(
        val documentId: DocumentId,
    ) : VoiceSessionState()

    data class Saved(
        val documentId: DocumentId,
    ) : VoiceSessionState()

    data class Error(
        val message: String,
        val recoverable: Boolean = true,
        val previous: VoiceSessionState? = null,
    ) : VoiceSessionState()
}

sealed class SpeechError(open val message: String) {
    data class NotAvailable(override val message: String = "Speech recognition is not available on this device") :
        SpeechError(message)

    data class PermissionDenied(override val message: String = "Microphone permission is required") :
        SpeechError(message)

    data class RecognitionFailed(override val message: String, val code: Int? = null) :
        SpeechError(message)

    data class Cancelled(override val message: String = "Listening cancelled") :
        SpeechError(message)
}
