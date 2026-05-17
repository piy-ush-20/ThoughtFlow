package com.piyush.thoughtflow.domain.session

import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Editing
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Error
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Formatting
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Idle
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Listening
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Saved
import com.piyush.thoughtflow.domain.model.VoiceSessionState.Transcribing
import com.piyush.thoughtflow.domain.model.Transcript
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoiceSessionStateMachineTest {

    private val machine = VoiceSessionStateMachine()

    @Test
    fun startListening_fromIdle_goesToListening() {
        val next = machine.transition(Idle, VoiceSessionEvent.StartListening)
        assertTrue(next is Listening)
    }

    @Test
    fun transcriptUpdate_promotesListeningToTranscribing() {
        val listening = Listening()
        val next = machine.transition(
            listening,
            VoiceSessionEvent.TranscriptUpdated(Transcript(partialText = "hello"), 0.5f),
        )
        assertTrue(next is Transcribing)
        assertEquals("hello", (next as Transcribing).transcript.partialText)
    }

    @Test
    fun finishListening_goesToFormatting() {
        val current = Transcribing(Transcript(finalText = "done"))
        val next = machine.transition(current, VoiceSessionEvent.FinishListening)
        assertTrue(next is Formatting)
    }

    @Test
    fun formattingComplete_goesToEditing() {
        val id = DocumentId("doc-1")
        val next = machine.transition(
            Formatting(Transcript(finalText = "x")),
            VoiceSessionEvent.FormattingComplete(id),
        )
        assertEquals(Editing(id), next)
    }

    @Test
    fun fail_createsRecoverableError() {
        val next = machine.transition(
            Listening(),
            VoiceSessionEvent.Fail("boom"),
        )
        assertTrue(next is Error)
        assertTrue((next as Error).recoverable)
    }

    @Test
    fun saved_thenReset_returnsIdle() {
        val id = DocumentId("a")
        val saved = machine.transition(Editing(id), VoiceSessionEvent.Saved(id))
        assertEquals(Saved(id), saved)
        val idle = machine.transition(saved, VoiceSessionEvent.Reset)
        assertEquals(Idle, idle)
    }
}
