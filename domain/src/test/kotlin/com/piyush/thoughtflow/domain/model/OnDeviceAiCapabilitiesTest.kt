package com.piyush.thoughtflow.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnDeviceAiCapabilitiesTest {

    @Test
    fun summaryLabelForReadyNano() {
        val caps = OnDeviceAiCapabilities(
            geminiNanoStatus = OnDeviceFeatureStatus.Available,
            genAiSdkPresent = true,
            aiCoreInstalled = true,
        )
        assertTrue(caps.geminiNanoReady)
        assertEquals("Gemini Nano ready", caps.summaryLabel())
    }

    @Test
    fun summaryLabelForMissingSdkWithAiCore() {
        val caps = OnDeviceAiCapabilities(
            aiCoreInstalled = true,
            aiCoreIsStub = false,
            genAiSdkPresent = false,
            geminiNanoStatus = OnDeviceFeatureStatus.SdkMissing,
        )
        assertFalse(caps.geminiNanoReady)
        assertEquals(
            "AICore present; GenAI SDK not linked in this build",
            caps.summaryLabel(),
        )
    }

    @Test
    fun summaryLabelForStubAiCore() {
        val caps = OnDeviceAiCapabilities(
            aiCoreInstalled = true,
            aiCoreIsStub = true,
            geminiNanoStatus = OnDeviceFeatureStatus.SdkMissing,
        )
        assertEquals(
            "AICore stub only — waiting for Play system update",
            caps.summaryLabel(),
        )
    }
}
