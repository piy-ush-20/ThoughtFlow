package com.piyush.thoughtflow.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.domain.usecase.ExportDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.GetDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase
import com.piyush.thoughtflow.export.ExportRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditorUiState(
    val documentId: String = "",
    val title: String = "",
    val body: String = "",
    val formatterUsed: String? = null,
    val message: String? = null,
    val loaded: Boolean = false,
)

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDocument: GetDocumentUseCase,
    private val saveDocument: SaveDocumentUseCase,
    private val exportDocument: ExportDocumentUseCase,
    private val exportRepository: ExportRepositoryImpl,
) : ViewModel() {

    private val documentId = DocumentId(checkNotNull(savedStateHandle["documentId"]))

    private val _uiState = MutableStateFlow(EditorUiState(documentId = documentId.value))
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDocument.observe(documentId).filterNotNull().collect { doc ->
                if (!_uiState.value.loaded) {
                    _uiState.update {
                        it.copy(
                            title = doc.title,
                            body = doc.bodyMarkdown,
                            formatterUsed = doc.formatterUsed,
                            loaded = true,
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onBodyChange(value: String) {
        _uiState.update { it.copy(body = value) }
    }

    fun save() {
        viewModelScope.launch {
            runCatching {
                saveDocument.updateContent(
                    id = documentId,
                    title = _uiState.value.title,
                    bodyMarkdown = _uiState.value.body,
                )
            }.onSuccess {
                _uiState.update { it.copy(message = "Saved") }
            }.onFailure { e ->
                _uiState.update { it.copy(message = e.message ?: "Save failed") }
            }
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun enqueueExport(format: ExportFormat) {
        exportDocument.enqueue(documentId, format)
        _uiState.update { it.copy(message = "Export queued") }
    }

    suspend fun exportAndShare(
        format: ExportFormat,
        share: (path: String, mime: String, exporter: ExportRepositoryImpl) -> Unit,
    ) {
        runCatching {
            val result = exportDocument(documentId, format)
            share(result.filePath, result.mimeType, exportRepository)
        }.onFailure { e ->
            _uiState.update { it.copy(message = e.message ?: "Export failed") }
        }
    }
}
