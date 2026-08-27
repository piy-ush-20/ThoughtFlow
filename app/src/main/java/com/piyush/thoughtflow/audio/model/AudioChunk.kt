package com.piyush.thoughtflow.audio.model

data class AudioChunk(
    val samples: ShortArray,
    val timestampMs: Long,
    val rms: Float,
    val peak: Float
)