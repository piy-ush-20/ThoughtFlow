package com.piyush.thoughtflow.navigation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piyush.thoughtflow.domain.model.AiPreferences
import com.piyush.thoughtflow.domain.model.OnDeviceAiCapabilities
import com.piyush.thoughtflow.domain.repository.SettingsRepository
import com.piyush.thoughtflow.domain.usecase.DetectOnDeviceAiCapabilitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val detectOnDeviceAiCapabilities: DetectOnDeviceAiCapabilitiesUseCase,
) : ViewModel() {

    val preferences: StateFlow<AiPreferences> = settingsRepository.preferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AiPreferences())

    private val _capabilities = MutableStateFlow(OnDeviceAiCapabilities())
    val capabilities: StateFlow<OnDeviceAiCapabilities> = _capabilities.asStateFlow()

    init {
        refreshCapabilities()
    }

    fun refreshCapabilities() {
        viewModelScope.launch {
            _capabilities.value = runCatching {
                detectOnDeviceAiCapabilities()
            }.getOrDefault(OnDeviceAiCapabilities())
        }
    }

    fun setPreferOnDevice(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.updatePreferences { it.copy(preferOnDevice = value) }
        }
    }

    fun setAllowCloud(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.updatePreferences { it.copy(allowCloud = value) }
        }
    }

    fun setCloudBaseUrl(value: String) {
        viewModelScope.launch {
            settingsRepository.updatePreferences { it.copy(cloudBaseUrl = value) }
        }
    }

    fun setCloudModel(value: String) {
        viewModelScope.launch {
            settingsRepository.updatePreferences { it.copy(cloudModel = value) }
        }
    }

    fun saveApiKey(apiKey: String?) {
        viewModelScope.launch {
            settingsRepository.setCloudApiKey(apiKey)
        }
    }
}
