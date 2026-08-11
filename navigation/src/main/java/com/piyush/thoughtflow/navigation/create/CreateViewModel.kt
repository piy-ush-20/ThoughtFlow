package com.piyush.thoughtflow.navigation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.usecase.SaveDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val saveDocument: SaveDocumentUseCase,
) : ViewModel() {

    fun createBlank(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val doc = saveDocument(
                FormattedDocument(
                    title = "Untitled",
                    bodyMarkdown = "# Untitled\n\n",
                    formatterId = "blank",
                ),
            )
            onCreated(doc.id.value)
        }
    }
}
