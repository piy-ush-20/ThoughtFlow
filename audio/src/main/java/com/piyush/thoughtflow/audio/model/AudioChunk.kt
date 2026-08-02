package com.piyush.thoughtflow.audio.model

/**
 * PCM audio chunk for future speech engines that consume raw samples.
 * Not used alongside platform SpeechRecognizer (mic contention).
 */
data class AudioChunk(
    val samples: ShortArray,
    val timestampMs: Long,
    val rms: Float,
    val peak: Float,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AudioChunk) return false
        return timestampMs == other.timestampMs &&
            rms == other.rms &&
            peak == other.peak &&
            samples.contentEquals(other.samples)
    }

    override fun hashCode(): Int {
        var result = samples.contentHashCode()
        result = 31 * result + timestampMs.hashCode()
        result = 31 * result + rms.hashCode()
        result = 31 * result + peak.hashCode()
        return result
    }
}
