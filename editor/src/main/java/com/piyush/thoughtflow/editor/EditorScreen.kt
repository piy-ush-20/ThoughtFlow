package com.piyush.thoughtflow.editor

import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.ExportFormat
import com.piyush.thoughtflow.export.ExportRepositoryImpl
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassIconButton
import kotlinx.coroutines.launch

@Composable
fun EditorRoute(
    onBack: () -> Unit,
    viewModel: EditorViewModel = hiltViewModel(),
) {
    val colors = ThoughtFlowTheme.colors
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var exportMenu by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbar.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }

    CosmicBackground(animated = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassIconButton(onClick = onBack, contentDescription = "Back") {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, tint = colors.textPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text("Editor", color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = viewModel::save) {
                    Text("Save", color = colors.primary, fontWeight = FontWeight.SemiBold)
                }
                IconButton(onClick = { exportMenu = true }) {
                    Icon(Icons.Outlined.Share, contentDescription = "Export", tint = colors.textPrimary)
                }
                DropdownMenu(
                    expanded = exportMenu,
                    onDismissRequest = { exportMenu = false },
                    containerColor = colors.surface,
                ) {
                    DropdownMenuItem(
                        text = { Text("Share Markdown", color = colors.textPrimary) },
                        onClick = {
                            exportMenu = false
                            scope.launch {
                                viewModel.exportAndShare(ExportFormat.Markdown) { path, mime, exporter ->
                                    shareFile(context, path, mime, exporter)
                                }
                            }
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Share Plain text", color = colors.textPrimary) },
                        onClick = {
                            exportMenu = false
                            scope.launch {
                                viewModel.exportAndShare(ExportFormat.PlainText) { path, mime, exporter ->
                                    shareFile(context, path, mime, exporter)
                                }
                            }
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Queue Markdown export", color = colors.textPrimary) },
                        onClick = {
                            exportMenu = false
                            viewModel.enqueueExport(ExportFormat.Markdown)
                        },
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                val fieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.border,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedContainerColor = colors.surfaceElevated,
                    unfocusedContainerColor = colors.surfaceElevated,
                    focusedLabelColor = colors.textSecondary,
                    unfocusedLabelColor = colors.textMuted,
                    cursorColor = colors.primary,
                )
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    singleLine = true,
                    colors = fieldColors,
                )
                OutlinedTextField(
                    value = uiState.body,
                    onValueChange = viewModel::onBodyChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    label = { Text("Markdown") },
                    minLines = 16,
                    colors = fieldColors,
                )
                uiState.formatterUsed?.let {
                    Text(
                        text = "Formatted by: $it",
                        color = colors.textMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(colors.surfaceElevated)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.Title, null, tint = colors.textSecondary, modifier = Modifier.size(22.dp))
                Icon(Icons.Outlined.FormatBold, null, tint = colors.textSecondary, modifier = Modifier.size(22.dp))
                Icon(Icons.AutoMirrored.Outlined.FormatListBulleted, null, tint = colors.textSecondary, modifier = Modifier.size(22.dp))
                Icon(Icons.Outlined.AutoAwesome, null, tint = colors.primary, modifier = Modifier.size(22.dp))
            }

            SnackbarHost(snackbar, Modifier.padding(8.dp))
            Box(Modifier.height(8.dp))
        }
    }
}

private fun shareFile(
    context: android.content.Context,
    path: String,
    mime: String,
    exporter: ExportRepositoryImpl,
) {
    val uri = exporter.uriFor(path)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mime
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share document"))
}
