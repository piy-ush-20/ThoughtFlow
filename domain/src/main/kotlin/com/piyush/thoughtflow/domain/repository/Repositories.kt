package com.piyush.thoughtflow.domain.repository

import com.piyush.thoughtflow.domain.model.AiPreferences
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.domain.model.ExportResult
import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.model.SpeechError
import com.piyush.thoughtflow.domain.model.Transcript
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SpeechRepository {
    val transcript: StateFlow<Transcript>
    val audioLevel: StateFlow<Float>
    val errors: SharedFlow<SpeechError>
    val isListening: StateFlow<Boolean>

    fun isAvailable(): Boolean
    fun startListening()
    fun stopListening()
    fun cancel()
    fun clearTranscript()
    fun destroy()
}

interface AIRepository {
    suspend fun format(text: String): FormattedDocument
}

interface DocumentRepository {
    fun observeDocuments(): Flow<List<Document>>
    fun observeDocument(id: DocumentId): Flow<Document?>
    suspend fun getDocument(id: DocumentId): Document?
    suspend fun upsert(document: Document)
    suspend fun delete(id: DocumentId)
    suspend fun markExported(id: DocumentId, exportedAtEpochMs: Long)
}

interface ExportRepository {
    suspend fun export(document: Document, format: ExportFormat): ExportResult
    fun enqueueExport(documentId: DocumentId, format: ExportFormat)
}

interface SettingsRepository {
    val preferences: Flow<AiPreferences>
    suspend fun updatePreferences(transform: (AiPreferences) -> AiPreferences)
    suspend fun setCloudApiKey(apiKey: String?)
    suspend fun getCloudApiKey(): String?
}
