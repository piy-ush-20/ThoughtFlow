package com.piyush.thoughtflow.navigation.create

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.components.QuickActionTile
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.BrandGradient
import com.piyush.thoughtflow.ui.theme.CosmicSurfaceElevated
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

@Composable
fun CreateRoute(
    onVoiceInput: () -> Unit,
    onBlankDocument: (String) -> Unit,
    onOpenTemplates: () -> Unit,
    contentBottomPadding: Int = 0,
    viewModel: CreateViewModel = hiltViewModel(),
) {
    CreateScreen(
        onVoiceInput = onVoiceInput,
        onTextInput = {
            viewModel.createBlank { id -> onBlankDocument(id) }
        },
        onBlankDocument = {
            viewModel.createBlank { id -> onBlankDocument(id) }
        },
        onOpenTemplates = onOpenTemplates,
        contentBottomPadding = contentBottomPadding,
    )
}

@Composable
fun CreateScreen(
    onVoiceInput: () -> Unit,
    onTextInput: () -> Unit,
    onBlankDocument: () -> Unit,
    onOpenTemplates: () -> Unit,
    contentBottomPadding: Int = 0,
) {
    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = contentBottomPadding.dp + 16.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Create", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Choose how you want to start",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 22.dp),
            )

            GlassCard(
                onClick = onVoiceInput,
                contentPadding = PaddingValues(22.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(BrandGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.Mic, null, tint = TextPrimary)
                    }
                    Spacer(Modifier.size(16.dp))
                    Column {
                        Text("Voice Input", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text(
                            "Speak naturally — AI structures it",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            GlassCard(
                onClick = onTextInput,
                contentPadding = PaddingValues(22.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(CosmicSurfaceElevated),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.Description, null, tint = BlueElectric)
                    }
                    Spacer(Modifier.size(16.dp))
                    Column {
                        Text("Text Input", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text(
                            "Start with a blank canvas",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
            Text("More options", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionTile(
                    title = "Blank Doc",
                    icon = Icons.Outlined.Description,
                    onClick = onBlankDocument,
                    modifier = Modifier.weight(1f),
                    accent = PurplePrimary,
                )
                QuickActionTile(
                    title = "Templates",
                    icon = Icons.Outlined.UploadFile,
                    onClick = onOpenTemplates,
                    modifier = Modifier.weight(1f),
                    accent = BlueElectric,
                )
                QuickActionTile(
                    title = "From Link",
                    icon = Icons.Outlined.Link,
                    onClick = onOpenTemplates,
                    modifier = Modifier.weight(1f),
                    accent = PurplePrimary,
                )
            }
        }
    }
}
