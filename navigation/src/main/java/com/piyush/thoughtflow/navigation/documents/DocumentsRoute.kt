package com.piyush.thoughtflow.navigation.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.Document
import com.piyush.thoughtflow.navigation.history.HistoryViewModel
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.FilterChip
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.theme.CosmicSurface
import com.piyush.thoughtflow.ui.theme.DangerRed
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsRoute(
    onOpenDocument: (String) -> Unit,
    contentBottomPadding: Int = 0,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf("All") }
    var menuDoc by remember { mutableStateOf<Document?>(null) }
    val sheetState = rememberModalBottomSheetState()

    DocumentsScreen(
        documents = documents,
        selectedFilter = selectedFilter,
        onFilterChange = { selectedFilter = it },
        onOpenDocument = onOpenDocument,
        onOpenMenu = { menuDoc = it },
        contentBottomPadding = contentBottomPadding,
    )

    menuDoc?.let { doc ->
        ModalBottomSheet(
            onDismissRequest = { menuDoc = null },
            sheetState = sheetState,
            containerColor = CosmicSurface,
        ) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(doc.title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                MenuRow("Open", onClick = {
                    menuDoc = null
                    onOpenDocument(doc.id.value)
                })
                MenuRow("Delete", tint = DangerRed, onClick = {
                    viewModel.delete(doc.id)
                    menuDoc = null
                })
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DocumentsScreen(
    documents: List<Document>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onOpenDocument: (String) -> Unit,
    onOpenMenu: (Document) -> Unit,
    contentBottomPadding: Int = 0,
) {
    val filters = listOf("All", "My Documents", "Shared", "Favorites")
    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Documents", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters.size) { index ->
                    val label = filters[index]
                    FilterChip(
                        label = label,
                        selected = selectedFilter == label,
                        onClick = { onFilterChange(label) },
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            if (documents.isEmpty()) {
                GlassCard {
                    Text(
                        "No documents yet. Capture a thought from Create → Voice Input.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = contentBottomPadding.dp + 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(documents, key = { it.id.value }) { doc ->
                        DocumentRow(
                            document = doc,
                            onClick = { onOpenDocument(doc.id.value) },
                            onMenu = { onOpenMenu(doc) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentRow(
    document: Document,
    onClick: () -> Unit,
    onMenu: () -> Unit,
) {
    val date = remember(document.updatedAtEpochMs) {
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date(document.updatedAtEpochMs))
    }
    GlassCard(
        onClick = onClick,
        contentPadding = PaddingValues(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PurplePrimary.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Description, null, tint = PurplePrimary)
            }
            Spacer(Modifier.size(12.dp))
            Column(Modifier.weight(1f)) {
                Text(document.title.ifBlank { "Untitled" }, color = TextPrimary, fontWeight = FontWeight.Medium)
                Text(
                    "Edited $date · ${document.wordCount} words",
                    color = TextMuted,
                    fontSize = 12.sp,
                )
            }
            IconButton(onClick = onMenu) {
                Icon(Icons.Outlined.IosShare, contentDescription = "Actions", tint = TextMuted)
            }
        }
    }
}

@Composable
private fun MenuRow(label: String, onClick: () -> Unit, tint: androidx.compose.ui.graphics.Color = TextPrimary) {
    Text(
        text = label,
        color = tint,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
    )
}
