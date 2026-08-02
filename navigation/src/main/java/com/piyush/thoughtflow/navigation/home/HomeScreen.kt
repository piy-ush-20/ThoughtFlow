package com.piyush.thoughtflow.navigation.home

import android.Manifest
import android.content.pm.PackageManager
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.VoiceSessionState
import com.piyush.thoughtflow.ui.components.GlassIconButton
import com.piyush.thoughtflow.ui.components.WaveBar
import com.piyush.thoughtflow.ui.theme.BgDeep
import com.piyush.thoughtflow.ui.theme.BgMid
import com.piyush.thoughtflow.ui.theme.GlassBorder
import com.piyush.thoughtflow.ui.theme.GlassWhite
import com.piyush.thoughtflow.ui.theme.NavyDark
import com.piyush.thoughtflow.ui.theme.NavyMid
import com.piyush.thoughtflow.ui.theme.TealBright
import com.piyush.thoughtflow.ui.theme.TealPale
import com.piyush.thoughtflow.ui.theme.TealSoft

@Composable
fun HomeRoute(
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onNavigateProcessing: () -> Unit,
    onNavigateEditor: (String) -> Unit,
    viewModel: RecordingViewModel = hiltViewModel(),
) {
    val state by viewModel.sessionState.collectAsStateWithLifecycle()
    val transcript by viewModel.transcript.collectAsStateWithLifecycle()
    val audioLevel by viewModel.audioLevel.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (val s = state) {
            is VoiceSessionState.Formatting -> onNavigateProcessing()
            is VoiceSessionState.Editing -> onNavigateEditor(s.documentId.value)
            else -> Unit
        }
    }

    HomeScreen(
        isCapturing = state is VoiceSessionState.Listening || state is VoiceSessionState.Transcribing,
        transcript = transcript,
        audioLevel = audioLevel,
        statusLabel = when (state) {
            is VoiceSessionState.Listening -> "Listening… tap mic to finish"
            is VoiceSessionState.Transcribing -> "Transcribing… tap mic to finish"
            is VoiceSessionState.Formatting -> "Formatting…"
            else -> "Your thoughts, captured"
        },
        onListClick = onOpenHistory,
        onSettingsClick = onOpenSettings,
        onMicClick = viewModel::onMicClicked,
        hasMicPermission = {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.RECORD_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED
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
fun HomeScreen(
    isCapturing: Boolean,
    transcript: String,
    audioLevel: Float,
    statusLabel: String,
    onListClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMicClick: () -> Unit,
    hasMicPermission: (android.content.Context) -> Boolean,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onMicClick()
        }
    }

    val dynamicScale = 1f + (audioLevel * 0.25f)
    val infiniteTransition = rememberInfiniteTransition(label = "tf_anim")

    val blobOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "blobFloat",
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCapturing) 1.18f else 1.04f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (isCapturing) 0.7f else 0.35f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w1",
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w2",
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w3",
    )

    val levelWave = (0.35f + audioLevel * 0.65f).coerceIn(0.2f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(BgMid, BgDeep, Color(0xFFC4D4E0)),
                    start = Offset(0f, 0f),
                    end = Offset(1080f, 1920f),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-60).dp + (blobOffset * 14).dp, y = 180.dp + (blobOffset * 10).dp)
                .blur(48.dp)
                .background(TealSoft.copy(alpha = 0.35f), RoundedCornerShape(50)),
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 180.dp - (blobOffset * 10).dp, y = 140.dp - (blobOffset * 8).dp)
                .blur(40.dp)
                .background(TealBright.copy(alpha = 0.30f), RoundedCornerShape(50)),
        )

        GlassIconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 52.dp),
            onClick = onListClick,
            contentDescription = "Open history",
        ) {
            Icon(Icons.AutoMirrored.Outlined.FormatListBulleted, null, tint = NavyDark, modifier = Modifier.size(22.dp))
        }

        GlassIconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 52.dp),
            onClick = onSettingsClick,
            contentDescription = "Settings",
        ) {
            Icon(Icons.Outlined.Settings, null, tint = NavyDark, modifier = Modifier.size(22.dp))
        }

        Text(
            text = "Thoughtflow",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = NavyDark,
                letterSpacing = 0.5.sp,
                shadow = Shadow(Color.White.copy(alpha = 0.6f), Offset(0f, 1f), 4f),
            ),
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(pulseScale * dynamicScale)
                        .background(TealPale.copy(alpha = pulseAlpha * 0.5f), CircleShape),
                )
                Box(
                    modifier = Modifier
                        .size(172.dp)
                        .scale(pulseScale)
                        .background(TealSoft.copy(alpha = pulseAlpha * 0.65f), CircleShape),
                )
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .clip(CircleShape)
                        .background(GlassWhite)
                        .border(1.5.dp, GlassBorder, CircleShape),
                )

                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NavyMid, NavyDark),
                                start = Offset(0f, 0f),
                                end = Offset(92f, 92f),
                            ),
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            val granted = hasMicPermission(context)
                            if (!granted) {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                return@clickable
                            }
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            onMicClick()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Mic,
                        contentDescription = if (isCapturing) "Tap to finish" else "Tap to speak",
                        tint = Color.White,
                        modifier = Modifier.size(38.dp),
                    )
                }
            }

            if (isCapturing) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WaveBar(fraction = wave1 * levelWave, color = TealBright)
                    WaveBar(fraction = wave2 * levelWave, color = TealSoft, maxHeight = 28.dp)
                    WaveBar(fraction = wave3 * levelWave, color = TealBright, maxHeight = 36.dp)
                    WaveBar(fraction = wave2 * levelWave, color = TealSoft, maxHeight = 28.dp)
                    WaveBar(fraction = wave1 * levelWave, color = TealBright)
                }
            } else {
                Text(
                    text = "Tap to speak",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = NavyDark.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp,
                    ),
                )
            }

            Text(
                text = transcript,
                modifier = Modifier.padding(horizontal = 32.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = NavyDark.copy(alpha = 0.75f),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                ),
                textAlign = TextAlign.Center,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .clip(RoundedCornerShape(50))
                .background(GlassWhite)
                .border(1.dp, GlassBorder, RoundedCornerShape(50))
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = statusLabel,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = NavyDark.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}
