package com.piyush.thoughtflow.navigation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.model.DocumentId
import com.piyush.thoughtflow.domain.usecase.DeleteDocumentUseCase
import com.piyush.thoughtflow.domain.usecase.ListDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    listDocuments: ListDocumentsUseCase,
    private val deleteDocument: DeleteDocumentUseCase,
) : ViewModel() {

    val documents: StateFlow<List<Document>> = listDocuments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(id: DocumentId) {
        viewModelScope.launch { deleteDocument(id) }
    }
}
