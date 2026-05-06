package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.FormattedDocument
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Always-available offline formatter. Turns free speech into readable markdown.
 */
@Singleton
class HeuristicDocumentFormatter @Inject constructor() : FormatterStrategy {

    override val id: String = ID

    override suspend fun isAvailable(): Boolean = true

    override suspend fun format(text: String): FormattedDocument {
        val cleaned = text
            .replace(Regex("\\s+"), " ")
            .trim()
        require(cleaned.isNotBlank()) { "Empty transcript" }

        val title = deriveTitle(cleaned)
        val body = buildMarkdown(cleaned, title)
        return FormattedDocument(
            title = title,
            bodyMarkdown = body,
            formatterId = id,
        )
    }

    private fun deriveTitle(text: String): String {
        val firstSentence = text.split(Regex("[.!?\\n]")).firstOrNull { it.isNotBlank() }?.trim()
            ?: text
        val truncated = if (firstSentence.length <= 48) {
            firstSentence
        } else {
            firstSentence.take(45).trimEnd() + "…"
        }
        return truncated.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    private fun buildMarkdown(text: String, title: String): String {
        val sentences = text
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val sb = StringBuilder()
        sb.append("# ").append(title).append("\n\n")

        val bullets = mutableListOf<String>()
        val paragraphs = mutableListOf<String>()

        for (sentence in sentences) {
            val lower = sentence.lowercase()
            val isBullet = BULLET_HINTS.any { lower.startsWith(it) } ||
                lower.matches(Regex("^(first|second|third|next|also|finally)[,:].*"))
            if (isBullet) {
                val content = sentence
                    .replace(Regex("^(?i)(todo|to do|action item|bullet|note)[:\\s-]+"), "")
                    .trim()
                bullets += content
            } else {
                paragraphs += sentence
            }
        }

        if (paragraphs.isNotEmpty()) {
            // Group into short paragraphs of ~2 sentences
            paragraphs.chunked(2).forEach { chunk ->
                sb.append(chunk.joinToString(" ")).append("\n\n")
            }
        }

        if (bullets.isNotEmpty()) {
            sb.append("## Key points\n\n")
            bullets.forEach { sb.append("- ").append(it).append('\n') }
            sb.append('\n')
        }

        if (paragraphs.isEmpty() && bullets.isEmpty()) {
            sb.append(text).append("\n")
        }

        return sb.toString().trimEnd() + "\n"
    }

    companion object {
        const val ID = "heuristic"
        private val BULLET_HINTS = listOf(
            "todo",
            "to do",
            "action item",
            "remember to",
            "don't forget",
            "bullet",
            "note that",
            "key point",
        )
    }
}
