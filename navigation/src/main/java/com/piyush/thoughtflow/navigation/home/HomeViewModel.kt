package com.piyush.thoughtflow.navigation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.domain.usecase.ListDocumentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    listDocuments: ListDocumentsUseCase,
) : ViewModel() {
    val recentDocuments: StateFlow<List<Document>> = listDocuments()
        .map { docs -> docs.sortedByDescending { it.updatedAtEpochMs } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
