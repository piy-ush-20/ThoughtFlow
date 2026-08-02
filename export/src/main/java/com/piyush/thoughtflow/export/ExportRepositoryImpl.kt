package com.piyush.thoughtflow.export

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.domain.model.ExportResult
import com.piyush.thoughtflow.domain.repository.DocumentRepository
import com.piyush.thoughtflow.domain.repository.ExportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val documentRepository: DocumentRepository,
    private val workScheduler: ExportWorkScheduler,
) : ExportRepository {

    override suspend fun export(document: Document, format: ExportFormat): ExportResult =
        withContext(Dispatchers.IO) {
            when (format) {
                ExportFormat.Markdown, ExportFormat.PlainText -> writeSimple(document, format)
                ExportFormat.Pdf, ExportFormat.Docx ->
                    error("${format::class.simpleName} export is not available in this version")
            }
        }

    override fun enqueueExport(documentId: DocumentId, format: ExportFormat) {
        workScheduler.enqueue(documentId, format)
    }

    private fun writeSimple(document: Document, format: ExportFormat): ExportResult {
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        // Privacy: exports live in cache; caller may share then we can delete later
        val (extension, mime, content) = when (format) {
            ExportFormat.Markdown -> Triple(
                "md",
                "text/markdown",
                document.bodyMarkdown,
            )
            ExportFormat.PlainText -> Triple(
                "txt",
                "text/plain",
                markdownToPlain(document.bodyMarkdown),
            )
            else -> error("Unsupported")
        }
        val safeTitle = document.title.replace(Regex("[^a-zA-Z0-9-_ ]"), "").trim().ifBlank { "document" }
        val file = File(dir, "${safeTitle}-${document.id.value.take(8)}.$extension")
        file.writeText(content)
        return ExportResult(
            filePath = file.absolutePath,
            mimeType = mime,
            format = format,
        )
    }

    fun uriFor(filePath: String): Uri {
        val file = File(filePath)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
    }

    private fun markdownToPlain(markdown: String): String =
        markdown
            .replace(Regex("^#+\\s*", RegexOption.MULTILINE), "")
            .replace(Regex("^[-*]\\s+", RegexOption.MULTILINE), "• ")
            .replace(Regex("\\*\\*(.+?)\\*\\*"), "$1")
            .replace(Regex("__(.+?)__"), "$1")
            .trim()
}
