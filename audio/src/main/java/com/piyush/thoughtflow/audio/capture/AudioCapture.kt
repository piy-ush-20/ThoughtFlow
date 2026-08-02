package com.piyush.thoughtflow.audio.capture

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.piyush.thoughtflow.audio.model.AudioChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * PCM capture for engines that accept raw audio.
 * Do not run in parallel with [android.speech.SpeechRecognizer].
 */
class AudioCapture(
    private val sampleRate: Int = 16_000,
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO,
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
) {
    private val _chunks = MutableSharedFlow<AudioChunk>(extraBufferCapacity = 64)
    val chunks: SharedFlow<AudioChunk> = _chunks

    private val _level = MutableSharedFlow<Float>(extraBufferCapacity = 16)
    val level: SharedFlow<Float> = _level

    var isCapturing: Boolean = false
        private set

    private var audioRecord: AudioRecord? = null
    private var captureJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val bufferSize: Int = maxOf(
        AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat) * 2,
        4096,
    )

    @Throws(SecurityException::class, IllegalStateException::class)
    fun start() {
        if (isCapturing) return

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize,
        ).also { record ->
            check(record.state == AudioRecord.STATE_INITIALIZED) {
                "AudioCapture failed to initialize. Check RECORD_AUDIO permission"
            }
            record.startRecording()
        }

        isCapturing = true

        captureJob = scope.launch {
            val buffer = ShortArray(bufferSize / 2)
            while (isActive && isCapturing) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: break
                if (read > 0) {
                    val slice = buffer.copyOf(read)
                    val rms = computeRms(slice)
                    val peak = computePeak(slice)
                    _chunks.tryEmit(
                        AudioChunk(
                            samples = slice,
                            timestampMs = System.currentTimeMillis(),
                            rms = rms,
                            peak = peak,
                        ),
                    )
                    _level.tryEmit(rms)
                }
            }
        }

        Log.d(TAG, "AudioCapture started sampleRate=$sampleRate")
    }

    fun stop() {
        isCapturing = false
        captureJob?.cancel()
        captureJob = null
        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
        Log.d(TAG, "AudioCapture stopped")
    }

    /** Clears any held buffers and releases hardware. */
    fun destroy() {
        stop()
        scope.cancel()
    }

    private fun computeRms(samples: ShortArray): Float {
        if (samples.isEmpty()) return 0f
        var sum = 0.0
        for (s in samples) {
            val n = s / 32768.0
            sum += n * n
        }
        return sqrt(sum / samples.size).toFloat().coerceIn(0f, 1f)
    }

    private fun computePeak(samples: ShortArray): Float {
        if (samples.isEmpty()) return 0f
        return (samples.maxOf { abs(it.toInt()) } / 32768f).coerceIn(0f, 1f)
    }

    companion object {
        private const val TAG = "AudioCapture"
    }
}
