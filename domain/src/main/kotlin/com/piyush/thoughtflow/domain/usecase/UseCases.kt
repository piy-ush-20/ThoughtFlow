package com.piyush.thoughtflow.domain.usecase

import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.DocumentStatus
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.domain.model.ExportResult
import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.model.OnDeviceAiCapabilities
import com.piyush.thoughtflow.domain.repository.AIRepository
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import com.piyush.thoughtflow.domain.repository.ExportRepository
import com.piyush.thoughtflow.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class StartListeningUseCase(
    private val speechRepository: SpeechRepository,
) {
    operator fun invoke() {
        speechRepository.clearTranscript()
        speechRepository.startListening()
    }
}

class StopListeningUseCase(
    private val speechRepository: SpeechRepository,
) {
    operator fun invoke() {
        speechRepository.stopListening()
    }
}

class FormatTranscriptUseCase(
    private val aiRepository: AIRepository,
) {
    suspend operator fun invoke(text: String): FormattedDocument {
        require(text.isNotBlank()) { "Transcript is empty" }
        return aiRepository.format(text.trim())
    }
}

class DetectOnDeviceAiCapabilitiesUseCase(
    private val aiRepository: AIRepository,
) {
    suspend operator fun invoke(): OnDeviceAiCapabilities =
        aiRepository.detectOnDeviceCapabilities()
}

class SaveDocumentUseCase(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(
        formatted: FormattedDocument,
        existingId: DocumentId? = null,
        nowEpochMs: Long = System.currentTimeMillis(),
    ): Document {
        val id = existingId ?: DocumentId(UUID.randomUUID().toString())
        val existing = existingId?.let { documentRepository.getDocument(it) }
        val wordCount = formatted.bodyMarkdown.split(Regex("\\s+")).count { it.isNotBlank() }
        val document = Document(
            id = id,
            title = formatted.title.ifBlank { "Untitled" },
            bodyMarkdown = formatted.bodyMarkdown,
            createdAtEpochMs = existing?.createdAtEpochMs ?: nowEpochMs,
            updatedAtEpochMs = nowEpochMs,
            status = DocumentStatus.Saved,
            wordCount = wordCount,
            lastExportAtEpochMs = existing?.lastExportAtEpochMs,
            formatterUsed = formatted.formatterId,
        )
        documentRepository.upsert(document)
        return document
    }

    suspend fun updateContent(
        id: DocumentId,
        title: String,
        bodyMarkdown: String,
        nowEpochMs: Long = System.currentTimeMillis(),
    ): Document {
        val existing = documentRepository.getDocument(id)
            ?: error("Document not found: ${id.value}")
        val updated = existing.copy(
            title = title.ifBlank { "Untitled" },
            bodyMarkdown = bodyMarkdown,
            updatedAtEpochMs = nowEpochMs,
            status = DocumentStatus.Saved,
            wordCount = bodyMarkdown.split(Regex("\\s+")).count { it.isNotBlank() },
        )
        documentRepository.upsert(updated)
        return updated
    }
}

class ListDocumentsUseCase(
    private val documentRepository: DocumentRepository,
) {
    operator fun invoke(): Flow<List<Document>> = documentRepository.observeDocuments()
}

class GetDocumentUseCase(
    private val documentRepository: DocumentRepository,
) {
    fun observe(id: DocumentId): Flow<Document?> = documentRepository.observeDocument(id)
    suspend fun once(id: DocumentId): Document? = documentRepository.getDocument(id)
}

class ExportDocumentUseCase(
    private val documentRepository: DocumentRepository,
    private val exportRepository: ExportRepository,
) {
    suspend operator fun invoke(id: DocumentId, format: ExportFormat): ExportResult {
        val document = documentRepository.getDocument(id)
            ?: error("Document not found: ${id.value}")
        val result = exportRepository.export(document, format)
        documentRepository.markExported(id, System.currentTimeMillis())
        return result
    }

    fun enqueue(id: DocumentId, format: ExportFormat) {
        exportRepository.enqueueExport(id, format)
    }
}

class DeleteDocumentUseCase(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(id: DocumentId) {
        documentRepository.delete(id)
    }
}
