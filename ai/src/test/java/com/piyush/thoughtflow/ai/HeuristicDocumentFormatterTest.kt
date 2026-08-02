package com.piyush.thoughtflow.ai

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HeuristicDocumentFormatterTest {

    private val formatter = HeuristicDocumentFormatter()

    @Test
    fun formatsSpokenParagraphIntoMarkdown() = runBlocking {
        val result = formatter.format(
            "Team sync notes. We shipped the login screen. Todo call design about icons. Remember to update the readme.",
        )
        assertTrue(result.bodyMarkdown.startsWith("# "))
        assertTrue(result.bodyMarkdown.contains("## Key points") || result.bodyMarkdown.contains("- "))
        assertEquals(HeuristicDocumentFormatter.ID, result.formatterId)
    }

    @Test
    fun titleUsesFirstSentence() = runBlocking {
        val result = formatter.format("Weekly goals. Ship the voice pipeline. Write tests.")
        assertTrue(result.title.contains("Weekly", ignoreCase = true))
    }
}
