package com.piyush.thoughtflow.navigation.settings

import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.OnDeviceAiCapabilities
import com.piyush.thoughtflow.domain.model.OnDeviceFeatureStatus
import com.piyush.thoughtflow.domain.model.ThemeMode
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.components.GlassIconButton

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val colors = ThoughtFlowTheme.colors
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val capabilities by viewModel.capabilities.collectAsStateWithLifecycle()
    var apiKeyDraft by remember { mutableStateOf("") }

    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassIconButton(onClick = onBack, contentDescription = "Back") {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, tint = colors.textPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text("AI Preferences", color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.size(46.dp))
            }

            Text(
                "Providers are selected automatically. The UI never talks to a vendor SDK directly.",
                color = colors.textSecondary,
                fontSize = 13.sp,
            )

            OnDeviceCapabilitiesCard(
                capabilities = capabilities,
                onRefresh = viewModel::refreshCapabilities,
            )

            AppearanceSection(
                themeMode = prefs.themeMode,
                onThemeModeChange = viewModel::setThemeMode,
            )

            SettingsSwitchRow(
                title = "Prefer on-device",
                subtitle = "Try Gemini Nano, then offline heuristic",
                checked = prefs.preferOnDevice,
                onCheckedChange = viewModel::setPreferOnDevice,
            )
            SettingsSwitchRow(
                title = "Allow cloud LLM",
                subtitle = "Optional OpenAI-compatible fallback",
                checked = prefs.allowCloud,
                onCheckedChange = viewModel::setAllowCloud,
            )

            if (prefs.allowCloud) {
                GlassCard(contentPadding = PaddingValues(14.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SettingsField(
                            value = apiKeyDraft,
                            onValueChange = { apiKeyDraft = it },
                            label = "Cloud API key",
                            password = true,
                        )
                        SettingsField(
                            value = prefs.cloudBaseUrl,
                            onValueChange = viewModel::setCloudBaseUrl,
                            label = "Base URL",
                        )
                        SettingsField(
                            value = prefs.cloudModel,
                            onValueChange = viewModel::setCloudModel,
                            label = "Model",
                        )
                        Button(
                            onClick = { viewModel.saveApiKey(apiKeyDraft.ifBlank { null }) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                        ) {
                            Text("Save API key")
                        }
                    }
                }
            }

            Text(
                "Privacy: raw audio is never stored. Temporary files, if any, are deleted after processing.",
                color = colors.textMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun AppearanceSection(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    val colors = ThoughtFlowTheme.colors
    GlassCard(contentPadding = PaddingValues(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Appearance", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
            Text(
                "Day theme is the default product design. Night uses the celestial dark palette.",
                color = colors.textMuted,
                fontSize = 12.sp,
            )
            ThemeModeOption(
                label = "Day (Light)",
                selected = themeMode == ThemeMode.Light,
                onClick = { onThemeModeChange(ThemeMode.Light) },
            )
            ThemeModeOption(
                label = "Night (Dark)",
                selected = themeMode == ThemeMode.Dark,
                onClick = { onThemeModeChange(ThemeMode.Dark) },
            )
            ThemeModeOption(
                label = "System default",
                selected = themeMode == ThemeMode.System,
                onClick = { onThemeModeChange(ThemeMode.System) },
            )
        }
    }
}

@Composable
private fun ThemeModeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = ThoughtFlowTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = colors.textPrimary, modifier = Modifier.weight(1f))
        if (selected) {
            Text("Selected", color = colors.primary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun OnDeviceCapabilitiesCard(
    capabilities: OnDeviceAiCapabilities,
    onRefresh: () -> Unit,
) {
    val colors = ThoughtFlowTheme.colors
    GlassCard(contentPadding = PaddingValues(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "On-device AI",
                    color = colors.textPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onRefresh) {
                    Text("Refresh", color = colors.primary)
                }
            }
            Text(capabilities.summaryLabel(), color = colors.textSecondary, fontSize = 13.sp)
            CapabilityLine(
                "AICore",
                when {
                    !capabilities.aiCoreInstalled -> "Not installed"
                    capabilities.aiCoreIsStub -> "Stub (${capabilities.aiCoreVersionName})"
                    else -> capabilities.aiCoreVersionName ?: "Installed"
                },
            )
            CapabilityLine("Gemini Nano", geminiStatusLabel(capabilities.geminiNanoStatus))
            CapabilityLine(
                "GenAI SDK",
                if (capabilities.genAiSdkPresent) "Linked" else "Not linked in this build",
            )
            CapabilityLine(
                "On-device speech",
                if (capabilities.onDeviceSpeechAvailable) "Available" else "Unavailable",
            )
            CapabilityLine(
                "Heuristic formatter",
                if (capabilities.heuristicAvailable) "Always available" else "Unavailable",
            )
        }
    }
}

@Composable
private fun CapabilityLine(label: String, value: String) {
    val colors = ThoughtFlowTheme.colors
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), color = colors.textMuted, fontSize = 12.sp)
        Text(value, color = colors.textSecondary, fontSize = 12.sp)
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
    val colors = ThoughtFlowTheme.colors
    GlassCard(contentPadding = PaddingValues(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, color = colors.textPrimary, fontWeight = FontWeight.Medium)
                Text(subtitle, color = colors.textMuted, fontSize = 12.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = colors.primary,
                    checkedThumbColor = colors.textPrimary,
                ),
            )
        }
    }
}

@Composable
private fun SettingsField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    password: Boolean = false,
) {
    val colors = ThoughtFlowTheme.colors
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.border,
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            focusedContainerColor = colors.surfaceElevated,
            unfocusedContainerColor = colors.surfaceElevated,
            focusedLabelColor = colors.textSecondary,
            unfocusedLabelColor = colors.textMuted,
            cursorColor = colors.primary,
        ),
    )
}
