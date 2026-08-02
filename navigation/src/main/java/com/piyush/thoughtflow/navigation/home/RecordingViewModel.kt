package com.piyush.thoughtflow.navigation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.processing.VoiceDocumentPipeline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val pipeline: VoiceDocumentPipeline,
) : ViewModel() {

    val sessionState: StateFlow<VoiceSessionState> = pipeline.state

    val transcript: StateFlow<String> = pipeline.state
        .map { state ->
            when (state) {
                is VoiceSessionState.Listening -> state.transcript.displayText
                is VoiceSessionState.Transcribing -> state.transcript.displayText
                is VoiceSessionState.Formatting -> state.transcript.displayText
                else -> ""
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val audioLevel: StateFlow<Float> = pipeline.state
        .map { state ->
            when (state) {
                is VoiceSessionState.Listening -> state.audioLevel
                is VoiceSessionState.Transcribing -> state.audioLevel
                else -> 0f
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    val errorMessage: StateFlow<String?> = pipeline.state
        .map { state -> (state as? VoiceSessionState.Error)?.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun onHoldStart() {
        val current = sessionState.value
        if (current is VoiceSessionState.Idle ||
            current is VoiceSessionState.Saved ||
            current is VoiceSessionState.Error
        ) {
            pipeline.startSession()
        }
    }

    fun onHoldEnd() {
        val current = sessionState.value
        if (current is VoiceSessionState.Listening || current is VoiceSessionState.Transcribing) {
            pipeline.finishSession()
        }
    }

    fun dismissError() {
        pipeline.dismissError()
        pipeline.reset()
    }
}
