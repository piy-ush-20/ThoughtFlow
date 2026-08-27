package com.piyush.thoughtflow.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.audio.capture.AudioCapture
import com.piyush.thoughtflow.speech.SpeechRecognizerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    application: Application
): AndroidViewModel(application) {

    private val audioCapture = AudioCapture()
    private val speechRecognizer = SpeechRecognizerManager(application)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        observeAudioLevels()
        observeTranscript()
    }

    fun onMicClicked() {
        if (_uiState.value.isCapturing) {
            stopCapturing()
        } else {
            startCapturing()
        }
    }

    private fun startCapturing() {
        audioCapture.start()
        speechRecognizer.startListening()
        _uiState.update {
            it.copy(isCapturing = true)
        }
    }

    private fun stopCapturing() {
        audioCapture.stop()
        speechRecognizer.stopListening()
        _uiState.update {
            it.copy(
                isCapturing = false,
                audioLevel = 0f
            )
        }
    }

    private fun observeAudioLevels() {
        viewModelScope.launch {
            audioCapture.level.collect { level ->
                _uiState.update {
                    it.copy(audioLevel = level)
                }
            }
        }
    }

    private fun observeTranscript() {
        viewModelScope.launch {
            speechRecognizer.transcript.collect { text ->
                _uiState.update {
                    it.copy(
                        transcript = text
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioCapture.destroy()
        speechRecognizer.destroy()
    }
}