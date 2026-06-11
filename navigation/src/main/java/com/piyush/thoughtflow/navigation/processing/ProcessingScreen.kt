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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.navigation.home.RecordingViewModel
import com.piyush.thoughtflow.ui.theme.BgDeep
import com.piyush.thoughtflow.ui.theme.BgMid
import com.piyush.thoughtflow.ui.theme.NavyDark
import com.piyush.thoughtflow.ui.theme.TealBright

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

    ProcessingScreen()
}

@Composable
fun ProcessingScreen() {
    val infinite = rememberInfiniteTransition(label = "processing")
    val scale by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Reverse),
        label = "scale",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(BgMid, BgDeep),
                    start = Offset.Zero,
                    end = Offset(900f, 1600f),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(24.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .scale(scale)
                        .background(TealBright.copy(alpha = 0.25f), CircleShape),
                )
                CircularProgressIndicator(color = NavyDark)
            }
            Text(
                text = "Shaping your document…",
                color = NavyDark,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Structuring thoughts offline-first",
                color = NavyDark.copy(alpha = 0.6f),
                fontSize = 14.sp,
            )
        }
    }
}
