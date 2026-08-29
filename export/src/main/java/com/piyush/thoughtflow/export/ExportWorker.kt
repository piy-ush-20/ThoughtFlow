package com.piyush.thoughtflow.export

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportWorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun enqueue(documentId: DocumentId, format: ExportFormat) {
        val formatName = when (format) {
            ExportFormat.Markdown -> "md"
            ExportFormat.PlainText -> "txt"
            ExportFormat.Pdf -> "pdf"
            ExportFormat.Docx -> "docx"
        }
        val request = OneTimeWorkRequestBuilder<ExportWorker>()
            .setInputData(
                workDataOf(
                    ExportWorker.KEY_DOCUMENT_ID to documentId.value,
                    ExportWorker.KEY_FORMAT to formatName,
                ),
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "export-${documentId.value}-$formatName",
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }
}

@HiltWorker
class ExportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val documentRepository: DocumentRepository,
    private val exportRepository: ExportRepositoryImpl,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val id = inputData.getString(KEY_DOCUMENT_ID) ?: return Result.failure()
        val formatName = inputData.getString(KEY_FORMAT) ?: "md"
        val format = when (formatName) {
            "txt" -> ExportFormat.PlainText
            "pdf" -> ExportFormat.Pdf
            "docx" -> ExportFormat.Docx
            else -> ExportFormat.Markdown
        }
        val document = documentRepository.getDocument(DocumentId(id)) ?: return Result.failure()
        return try {
            val result = exportRepository.export(document, format)
            documentRepository.markExported(DocumentId(id), System.currentTimeMillis())
            Result.success(
                workDataOf(KEY_OUTPUT_PATH to result.filePath),
            )
        } catch (_: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val KEY_DOCUMENT_ID = "document_id"
        const val KEY_FORMAT = "format"
        const val KEY_OUTPUT_PATH = "output_path"
    }
}
