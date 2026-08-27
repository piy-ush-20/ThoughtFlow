package com.piyush.thoughtflow.navigation.voice

import android.Manifest
import android.content.pm.PackageManager
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.navigation.home.RecordingViewModel
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassIconButton
import com.piyush.thoughtflow.ui.components.GradientButton
import com.piyush.thoughtflow.ui.components.WaveBar
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.GlassBorder
import com.piyush.thoughtflow.ui.theme.GlassWhite
import com.piyush.thoughtflow.ui.theme.PurpleDeep
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.PurpleSoft
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

@Composable
fun VoiceRoute(
    onBack: () -> Unit,
    onNavigateProcessing: () -> Unit,
    onNavigateEditor: (String) -> Unit,
    viewModel: RecordingViewModel = hiltViewModel(),
) {
    val state by viewModel.sessionState.collectAsStateWithLifecycle()
    val transcript by viewModel.transcript.collectAsStateWithLifecycle()
    val audioLevel by viewModel.audioLevel.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isCapturing by viewModel.isCapturing.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (val s = state) {
            is VoiceSessionState.Formatting -> onNavigateProcessing()
            is VoiceSessionState.Editing -> onNavigateEditor(s.documentId.value)
            else -> Unit
        }
    }

    VoiceScreen(
        isCapturing = isCapturing,
        transcript = transcript,
        audioLevel = audioLevel,
        onBack = onBack,
        onMicClick = viewModel::onMicClicked,
        onFinish = {
            if (isCapturing) viewModel.onMicClicked()
        },
        hasMicPermission = {
            ContextCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        },
    )

    errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::dismissError,
            title = { Text("Something went wrong") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = viewModel::dismissError) { Text("OK") }
            },
        )
    }
}

@Composable
fun VoiceScreen(
    isCapturing: Boolean,
    transcript: String,
    audioLevel: Float,
    onBack: () -> Unit,
    onMicClick: () -> Unit,
    onFinish: () -> Unit,
    hasMicPermission: (android.content.Context) -> Boolean,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onMicClick()
        }
    }

    val infinite = rememberInfiniteTransition(label = "voice")
    val pulse by infinite.animateFloat(
        initialValue = 1f,
        targetValue = if (isCapturing) 1.16f else 1.05f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse",
    )
    val wave1 by infinite.animateFloat(0.3f, 1f, infiniteRepeatable(tween(500), RepeatMode.Reverse), "w1")
    val wave2 by infinite.animateFloat(1f, 0.3f, infiniteRepeatable(tween(700), RepeatMode.Reverse), "w2")
    val wave3 by infinite.animateFloat(0.5f, 0.95f, infiniteRepeatable(tween(600), RepeatMode.Reverse), "w3")
    val levelWave = (0.35f + audioLevel * 0.65f).coerceIn(0.2f, 1f)

    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassIconButton(onClick = onBack, contentDescription = "Back") {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, tint = TextPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = if (isCapturing) "Listening…" else "Voice Input",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(46.dp))
            }

            Spacer(Modifier.weight(0.35f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (isCapturing) "Listening…" else "Tap the mic to start",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Your audio stays private — never stored",
                    color = TextMuted,
                    fontSize = 13.sp,
                )

                Spacer(Modifier.height(36.dp))

                Box(contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size(220.dp)
                            .scale(pulse * (1f + audioLevel * 0.2f))
                            .background(PurplePrimary.copy(alpha = 0.18f), CircleShape),
                    )
                    Box(
                        Modifier
                            .size(170.dp)
                            .scale(pulse)
                            .background(PurpleSoft.copy(alpha = 0.22f), CircleShape),
                    )
                    Box(
                        Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(GlassWhite)
                            .border(1.dp, GlassBorder, CircleShape),
                    )
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PurpleDeep, PurplePrimary, BlueElectric)))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                if (!hasMicPermission(context)) {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    return@clickable
                                }
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                onMicClick()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.Mic, null, tint = TextPrimary, modifier = Modifier.size(36.dp))
                    }
                }

                if (isCapturing) {
                    Spacer(Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        WaveBar(wave1 * levelWave)
                        WaveBar(wave2 * levelWave, maxHeight = 28.dp)
                        WaveBar(wave3 * levelWave, maxHeight = 36.dp)
                        WaveBar(wave2 * levelWave, maxHeight = 28.dp)
                        WaveBar(wave1 * levelWave)
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text(
                    text = transcript.ifBlank { " " },
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    minLines = 2,
                )
            }

            Spacer(Modifier.weight(0.45f))

            if (isCapturing) {
                GradientButton(text = "Finish & Process", onClick = onFinish)
                Spacer(Modifier.height(28.dp))
            } else {
                Spacer(Modifier.height(82.dp))
            }
        }
    }
}
