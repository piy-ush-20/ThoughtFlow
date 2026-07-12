package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.OnDeviceFeatureStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnDeviceAiCapabilityDetectorTest {

    @Test
    fun reportsSdkMissingWhenGenAiClassesAbsent() = runBlocking {
        val detector = OnDeviceAiCapabilityDetector(
            FakeProbes(
                installed = true,
                versionName = "1.2.3",
                sdkPresent = false,
                geminiStatus = OnDeviceFeatureStatus.Available,
                speech = true,
            ),
        )
        val caps = detector.detect()
        assertTrue(caps.aiCoreInstalled)
        assertFalse(caps.aiCoreIsStub)
        assertFalse(caps.genAiSdkPresent)
        assertEquals(OnDeviceFeatureStatus.SdkMissing, caps.geminiNanoStatus)
        assertTrue(caps.onDeviceSpeechAvailable)
        assertTrue(caps.heuristicAvailable)
        assertFalse(caps.geminiNanoReady)
    }

    @Test
    fun detectsStubAiCorePackage() = runBlocking {
        val detector = OnDeviceAiCapabilityDetector(
            FakeProbes(
                installed = true,
                versionName = "0.stub-pixel",
                sdkPresent = false,
                geminiStatus = OnDeviceFeatureStatus.Unavailable,
                speech = false,
            ),
        )
        val caps = detector.detect()
        assertTrue(caps.aiCoreIsStub)
        assertTrue(caps.summaryLabel().contains("stub", ignoreCase = true))
    }

    @Test
    fun surfacesAvailableGeminiWhenSdkReportsReady() = runBlocking {
        val detector = OnDeviceAiCapabilityDetector(
            FakeProbes(
                installed = true,
                versionName = "1.4.0",
                sdkPresent = true,
                geminiStatus = OnDeviceFeatureStatus.Available,
                speech = true,
            ),
        )
        val caps = detector.detect()
        assertEquals(OnDeviceFeatureStatus.Available, caps.geminiNanoStatus)
        assertTrue(caps.geminiNanoReady)
        assertTrue(caps.geminiNanoSupported)
        assertEquals("Gemini Nano ready", caps.summaryLabel())
    }

    @Test
    fun mapsDownloadableStatus() = runBlocking {
        val detector = OnDeviceAiCapabilityDetector(
            FakeProbes(
                installed = true,
                versionName = "1.4.0",
                sdkPresent = true,
                geminiStatus = OnDeviceFeatureStatus.Downloadable,
                speech = false,
            ),
        )
        val caps = detector.detect()
        assertEquals(OnDeviceFeatureStatus.Downloadable, caps.geminiNanoStatus)
        assertTrue(caps.geminiNanoSupported)
        assertFalse(caps.geminiNanoReady)
    }

    @Test
    fun featureStatusMapperHandlesIntConstantsAndNames() {
        assertEquals(OnDeviceFeatureStatus.Unavailable, FeatureStatusMapper.map(0))
        assertEquals(OnDeviceFeatureStatus.Downloadable, FeatureStatusMapper.map(1))
        assertEquals(OnDeviceFeatureStatus.Downloading, FeatureStatusMapper.map(2))
        assertEquals(OnDeviceFeatureStatus.Available, FeatureStatusMapper.map(3))
        assertEquals(OnDeviceFeatureStatus.Unavailable, FeatureStatusMapper.map("UNAVAILABLE"))
        assertEquals(OnDeviceFeatureStatus.Available, FeatureStatusMapper.map("AVAILABLE"))
        assertEquals(OnDeviceFeatureStatus.Downloadable, FeatureStatusMapper.map("DOWNLOADABLE"))
    }

    private class FakeProbes(
        private val installed: Boolean,
        private val versionName: String?,
        private val sdkPresent: Boolean,
        private val geminiStatus: OnDeviceFeatureStatus,
        private val speech: Boolean,
    ) : DeviceAiProbes {
        override fun isAiCoreInstalled() = installed
        override fun aiCoreVersionName() = versionName
        override fun isGenAiSdkPresent() = sdkPresent
        override fun checkGeminiNanoFeatureStatus() = geminiStatus
        override fun isOnDeviceSpeechAvailable() = speech
    }
}
