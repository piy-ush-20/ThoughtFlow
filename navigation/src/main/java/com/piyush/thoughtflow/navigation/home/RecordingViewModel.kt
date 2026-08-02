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

    val isCapturing: StateFlow<Boolean> = pipeline.state
        .map { state ->
            state is VoiceSessionState.Listening || state is VoiceSessionState.Transcribing
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun onMicClicked() {
        when (sessionState.value) {
            is VoiceSessionState.Idle,
            is VoiceSessionState.Saved,
            is VoiceSessionState.Error,
            -> pipeline.startSession()

            is VoiceSessionState.Listening,
            is VoiceSessionState.Transcribing,
            -> pipeline.finishSession()

            is VoiceSessionState.Formatting,
            is VoiceSessionState.Editing,
            -> Unit // ignore taps while processing / editing navigation
        }
    }

    fun dismissError() {
        pipeline.dismissError()
        pipeline.reset()
    }
}
