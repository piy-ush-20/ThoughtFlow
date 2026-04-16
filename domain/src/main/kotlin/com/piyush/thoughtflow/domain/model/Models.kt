package com.piyush.thoughtflow.domain.model

@JvmInline
value class DocumentId(val value: String)

data class Transcript(
    val partialText: String = "",
    val finalText: String = "",
) {
    val displayText: String
        get() = when {
            partialText.isNotBlank() -> partialText
            else -> finalText
        }

    val committedText: String
        get() = finalText.ifBlank { partialText }
}

enum class DocumentStatus {
    Draft,
    Saved,
}

data class Document(
    val id: DocumentId,
    val title: String,
    val bodyMarkdown: String,
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long,
    val status: DocumentStatus = DocumentStatus.Draft,
    val wordCount: Int = 0,
    val lastExportAtEpochMs: Long? = null,
    val formatterUsed: String? = null,
)

data class FormattedDocument(
    val title: String,
    val bodyMarkdown: String,
    val formatterId: String,
)

sealed class ExportFormat {
    data object Markdown : ExportFormat()
    data object PlainText : ExportFormat()
    // Reserved for future
    data object Pdf : ExportFormat()
    data object Docx : ExportFormat()
}

data class ExportResult(
    val filePath: String,
    val mimeType: String,
    val format: ExportFormat,
)

data class AiPreferences(
    val preferOnDevice: Boolean = true,
    val allowCloud: Boolean = false,
    val cloudApiKey: String? = null,
    val cloudBaseUrl: String = "https://api.openai.com/v1",
    val cloudModel: String = "gpt-4o-mini",
)
