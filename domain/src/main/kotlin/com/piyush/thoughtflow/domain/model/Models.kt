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

/**
 * Runtime status for Gemini Nano / AICore feature readiness.
 * Mirrors ML Kit GenAI FeatureStatus plus ThoughtFlow-specific states.
 */
enum class OnDeviceFeatureStatus {
    /** Model downloaded and ready for on-device inference. */
    Available,

    /** Device supports Nano but the model is not downloaded yet. */
    Downloadable,

    /** Model download is already in progress. */
    Downloading,

    /** Hardware / AICore / feature not supported (or check failed). */
    Unavailable,

    /** GenAI Prompt SDK classes are not present on the app classpath. */
    SdkMissing,
}

/**
 * Snapshot of on-device AI / speech capabilities for adaptive routing and Settings.
 */
data class OnDeviceAiCapabilities(
    val aiCoreInstalled: Boolean = false,
    val aiCoreIsStub: Boolean = false,
    val aiCoreVersionName: String? = null,
    val genAiSdkPresent: Boolean = false,
    val geminiNanoStatus: OnDeviceFeatureStatus = OnDeviceFeatureStatus.Unavailable,
    val onDeviceSpeechAvailable: Boolean = false,
    val heuristicAvailable: Boolean = true,
) {
    val geminiNanoReady: Boolean
        get() = geminiNanoStatus == OnDeviceFeatureStatus.Available

    val geminiNanoSupported: Boolean
        get() = geminiNanoStatus == OnDeviceFeatureStatus.Available ||
            geminiNanoStatus == OnDeviceFeatureStatus.Downloadable ||
            geminiNanoStatus == OnDeviceFeatureStatus.Downloading

    fun summaryLabel(): String = when {
        geminiNanoReady -> "Gemini Nano ready"
        geminiNanoStatus == OnDeviceFeatureStatus.Downloading -> "Gemini Nano downloading"
        geminiNanoStatus == OnDeviceFeatureStatus.Downloadable -> "Gemini Nano downloadable"
        geminiNanoStatus == OnDeviceFeatureStatus.SdkMissing && aiCoreInstalled && !aiCoreIsStub ->
            "AICore present; GenAI SDK not linked in this build"
        geminiNanoStatus == OnDeviceFeatureStatus.SdkMissing && aiCoreIsStub ->
            "AICore stub only — waiting for Play system update"
        geminiNanoStatus == OnDeviceFeatureStatus.SdkMissing && !aiCoreInstalled ->
            "AICore not installed"
        else -> "On-device LLM unavailable — using heuristic formatter"
    }
}
