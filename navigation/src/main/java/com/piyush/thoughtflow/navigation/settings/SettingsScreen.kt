package com.piyush.thoughtflow.navigation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.OnDeviceAiCapabilities
import com.piyush.thoughtflow.domain.model.OnDeviceFeatureStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val capabilities by viewModel.capabilities.collectAsStateWithLifecycle()
    var apiKeyDraft by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("AI providers are selected automatically. The UI never talks to a vendor SDK directly.")

            OnDeviceCapabilitiesCard(
                capabilities = capabilities,
                onRefresh = viewModel::refreshCapabilities,
            )

            SettingsSwitchRow(
                title = "Prefer on-device",
                subtitle = "Try Gemini Nano, then offline heuristic",
                checked = prefs.preferOnDevice,
                onCheckedChange = viewModel::setPreferOnDevice,
            )
            SettingsSwitchRow(
                title = "Allow cloud LLM",
                subtitle = "Optional OpenAI-compatible fallback (off by default)",
                checked = prefs.allowCloud,
                onCheckedChange = viewModel::setAllowCloud,
            )

            if (prefs.allowCloud) {
                OutlinedTextField(
                    value = apiKeyDraft,
                    onValueChange = { apiKeyDraft = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Cloud API key") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = prefs.cloudBaseUrl,
                    onValueChange = viewModel::setCloudBaseUrl,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Base URL") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = prefs.cloudModel,
                    onValueChange = viewModel::setCloudModel,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Model") },
                    singleLine = true,
                )
                Button(
                    onClick = { viewModel.saveApiKey(apiKeyDraft.ifBlank { null }) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save API key")
                }
            }

            Text(
                "Privacy: raw audio is never stored. Temporary files, if any, are deleted after processing.",
            )
        }
    }
}

@Composable
private fun OnDeviceCapabilitiesCard(
    capabilities: OnDeviceAiCapabilities,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "On-device AI",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onRefresh) {
                Text("Refresh")
            }
        }
        Text(
            text = capabilities.summaryLabel(),
            style = MaterialTheme.typography.bodyMedium,
        )
        CapabilityLine(
            label = "AICore",
            value = when {
                !capabilities.aiCoreInstalled -> "Not installed"
                capabilities.aiCoreIsStub -> "Stub (${capabilities.aiCoreVersionName})"
                else -> capabilities.aiCoreVersionName ?: "Installed"
            },
        )
        CapabilityLine(
            label = "Gemini Nano",
            value = geminiStatusLabel(capabilities.geminiNanoStatus),
        )
        CapabilityLine(
            label = "GenAI SDK",
            value = if (capabilities.genAiSdkPresent) "Linked" else "Not linked in this build",
        )
        CapabilityLine(
            label = "On-device speech",
            value = if (capabilities.onDeviceSpeechAvailable) "Available" else "Unavailable",
        )
        CapabilityLine(
            label = "Heuristic formatter",
            value = if (capabilities.heuristicAvailable) "Always available" else "Unavailable",
        )
    }
}

@Composable
private fun CapabilityLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun geminiStatusLabel(status: OnDeviceFeatureStatus): String = when (status) {
    OnDeviceFeatureStatus.Available -> "Ready"
    OnDeviceFeatureStatus.Downloadable -> "Downloadable"
    OnDeviceFeatureStatus.Downloading -> "Downloading"
    OnDeviceFeatureStatus.Unavailable -> "Unavailable"
    OnDeviceFeatureStatus.SdkMissing -> "SDK missing"
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            Text(subtitle)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
