package com.piyush.thoughtflow.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TranscriptTest {

    @Test
    fun committedTextMatchesLivePartialWhenFinalAlreadyExists() {
        val t = Transcript(
            partialText = "Hello world how are you",
            finalText = "Hello world",
        )
        assertEquals("Hello world how are you", t.displayText)
        assertEquals("Hello world how are you", t.committedText)
    }

    @Test
    fun committedTextFallsBackToFinalWhenNoPartial() {
        val t = Transcript(partialText = "", finalText = "Done")
        assertEquals("Done", t.committedText)
    }
}
