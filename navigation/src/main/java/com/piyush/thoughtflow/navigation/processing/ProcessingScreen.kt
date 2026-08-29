package com.piyush.thoughtflow.navigation.processing

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.navigation.home.RecordingViewModel
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.PurpleDeep
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.SuccessGreen
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun ProcessingRoute(
    onNavigateEditor: (String) -> Unit,
    onBackHome: () -> Unit,
    viewModel: RecordingViewModel = hiltViewModel(),
) {
    val state by viewModel.sessionState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (val s = state) {
            is VoiceSessionState.Editing -> onNavigateEditor(s.documentId.value)
            is VoiceSessionState.Idle, is VoiceSessionState.Error -> onBackHome()
            else -> Unit
        }
    }

    ProcessingScreen(isFormatting = state is VoiceSessionState.Formatting)
}

@Composable
fun ProcessingScreen(isFormatting: Boolean = true) {
    val infinite = rememberInfiniteTransition(label = "processing")
    val scale by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Reverse),
        label = "scale",
    )
    var progress by remember { mutableFloatStateOf(0.18f) }
    LaunchedEffect(isFormatting) {
        while (true) {
            delay(450)
            progress = (progress + 0.08f).coerceAtMost(0.92f)
        }
    }

    val steps = listOf(
        "Understanding your input" to true,
        "Detecting on-device AI" to true,
        "Structuring content" to isFormatting,
        "Generating document" to false,
    )

    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(0.25f))
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale)
                        .background(
                            Brush.radialGradient(listOf(PurplePrimary.copy(alpha = 0.45f), PurpleDeep.copy(alpha = 0.05f))),
                            CircleShape,
                        ),
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(
                            Brush.linearGradient(listOf(PurpleDeep, PurplePrimary, BlueElectric)),
                            CircleShape,
                        ),
                )
            }
            Spacer(Modifier.height(28.dp))
            Text("Shaping your document…", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Structuring thoughts offline-first",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 6.dp),
            )
            Spacer(Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                steps.forEach { (label, done) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(if (done) SuccessGreen.copy(alpha = 0.2f) else PurplePrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (done) {
                                Icon(Icons.Outlined.Check, null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                            } else {
                                Box(
                                    Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(PurplePrimary),
                                )
                            }
                        }
                        Spacer(Modifier.size(12.dp))
                        Text(label, color = if (done) TextPrimary else TextMuted, fontSize = 14.sp)
                    }
                }
            }
            Spacer(Modifier.weight(0.35f))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = PurplePrimary,
                trackColor = PurplePrimary.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round,
            )
            Text(
                "${(progress * 100).toInt()}%",
                color = TextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 36.dp),
            )
        }
    }
}
