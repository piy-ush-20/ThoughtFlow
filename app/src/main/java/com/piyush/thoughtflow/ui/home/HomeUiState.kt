package com.piyush.thoughtflow.ui.home

data class HomeUiState(
    val isCapturing: Boolean = false,
    val audioLevel: Float = 0f,
    val transcript: String = ""
)
