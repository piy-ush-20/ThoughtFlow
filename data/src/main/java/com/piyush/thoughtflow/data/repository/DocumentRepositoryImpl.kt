package com.piyush.thoughtflow.data.repository

import com.piyush.thoughtflow.data.local.DocumentDao
import com.piyush.thoughtflow.data.local.DocumentEntity
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.DocumentStatus
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepositoryImpl @Inject constructor(
    private val dao: DocumentDao,
) : DocumentRepository {

    override fun observeDocuments(): Flow<List<Document>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeDocument(id: DocumentId): Flow<Document?> =
        dao.observeById(id.value).map { it?.toDomain() }

    override suspend fun getDocument(id: DocumentId): Document? =
        dao.getById(id.value)?.toDomain()

    override suspend fun upsert(document: Document) {
        dao.upsert(document.toEntity())
    }

    override suspend fun delete(id: DocumentId) {
        dao.delete(id.value)
    }

    override suspend fun markExported(id: DocumentId, exportedAtEpochMs: Long) {
        dao.markExported(id.value, exportedAtEpochMs)
    }

    private fun DocumentEntity.toDomain(): Document = Document(
        id = DocumentId(id),
        title = title,
        bodyMarkdown = bodyMarkdown,
        createdAtEpochMs = createdAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
        status = runCatching { DocumentStatus.valueOf(status) }.getOrDefault(DocumentStatus.Draft),
        wordCount = wordCount,
        lastExportAtEpochMs = lastExportAtEpochMs,
        formatterUsed = formatterUsed,
    )

    private fun Document.toEntity(): DocumentEntity = DocumentEntity(
        id = id.value,
        title = title,
        bodyMarkdown = bodyMarkdown,
        createdAtEpochMs = createdAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
        status = status.name,
        wordCount = wordCount,
        lastExportAtEpochMs = lastExportAtEpochMs,
        formatterUsed = formatterUsed,
    )
}
