package com.piyush.thoughtflow.navigation.home

import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.components.QuickActionTile
import com.piyush.thoughtflow.ui.components.SearchField
import com.piyush.thoughtflow.ui.components.SectionHeader
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun HomeRoute(
    onOpenDocuments: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenCreate: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenDocument: (String) -> Unit,
    contentBottomPadding: Int = 0,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val documents by viewModel.recentDocuments.collectAsStateWithLifecycle()
    HomeScreen(
        documents = documents,
        onOpenDocuments = onOpenDocuments,
        onOpenVoice = onOpenVoice,
        onOpenCreate = onOpenCreate,
        onOpenTemplates = onOpenTemplates,
        onOpenDocument = onOpenDocument,
        contentBottomPadding = contentBottomPadding,
    )
}

@Composable
fun HomeScreen(
    documents: List<Document>,
    onOpenDocuments: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenCreate: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenDocument: (String) -> Unit,
    contentBottomPadding: Int = 0,
) {
    val colors = ThoughtFlowTheme.colors
    var query by remember { mutableStateOf("") }
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = contentBottomPadding.dp + 16.dp),
        ) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "$greeting 👋",
                        color = colors.textPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Speak. Structure. Create.",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(colors.brandGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("TF", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(20.dp))
            SearchField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search documents",
                trailing = {
                    Icon(Icons.Outlined.Tune, contentDescription = null, tint = colors.textMuted, modifier = Modifier.size(20.dp))
                },
            )

            Spacer(Modifier.height(24.dp))
            GlassCard(
                onClick = onOpenCreate,
                contentPadding = PaddingValues(20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Create New", color = colors.textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text(
                            "Turn voice into structured docs",
                            color = colors.textSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(colors.brandGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.Add, null, tint = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            SectionHeader(title = "Quick Actions")
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                QuickActionTile(
                    title = "Voice Input",
                    icon = Icons.Outlined.Mic,
                    onClick = onOpenVoice,
                    modifier = Modifier.weight(1f),
                    accent = colors.primary,
                )
                QuickActionTile(
                    title = "Create New",
                    icon = Icons.Outlined.Add,
                    onClick = onOpenCreate,
                    modifier = Modifier.weight(1f),
                    accent = colors.accentBlue,
                )
                QuickActionTile(
                    title = "Templates",
                    icon = Icons.Outlined.FileUpload,
                    onClick = onOpenTemplates,
                    modifier = Modifier.weight(1f),
                    accent = colors.accentPink,
                )
            }

            Spacer(Modifier.height(26.dp))
            SectionHeader(
                title = "Continue Editing",
                actionLabel = "See all",
                onAction = onOpenDocuments,
            )
            Spacer(Modifier.height(12.dp))
            if (documents.isEmpty()) {
                GlassCard {
                    Text(
                        "No documents yet. Tap Voice Input to capture your first thought.",
                        color = colors.textSecondary,
                        fontSize = 13.sp,
                    )
                }
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(documents.take(8), key = { it.id.value }) { doc ->
                        ContinueCard(doc) { onOpenDocument(doc.id.value) }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ContinueCard(document: Document, onClick: () -> Unit) {
    val colors = ThoughtFlowTheme.colors
    val date = remember(document.updatedAtEpochMs) {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date(document.updatedAtEpochMs))
    }
    GlassCard(
        modifier = Modifier.width(200.dp),
        onClick = onClick,
        contentPadding = PaddingValues(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Description, null, tint = colors.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = document.title.ifBlank { "Untitled" },
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1,
                )
                Text("Edited $date", color = colors.textMuted, fontSize = 11.sp, maxLines = 1)
            }
        }
    }
}
