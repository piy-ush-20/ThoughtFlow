package com.piyush.thoughtflow.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.piyush.thoughtflow.ui.components.WaveBar
import com.piyush.thoughtflow.ui.components.GlassIconButton

private val BgDeep          = Color(0xFFB8C8D8)   // cool light-grey base
private val BgMid           = Color(0xFFD0DDE8)
private val NavyDark        = Color(0xFF2C3E55)    // mic body / wordmark
private val NavyMid         = Color(0xFF3D5068)
private val TealBright      = Color(0xFF4BA8A8)    // bright right blob
private val TealSoft        = Color(0xFF8DCACB)    // soft left blob
private val TealPale        = Color(0xFFB4DDE0)    // ghost ring

// Glass surface colours
private val GlassWhite      = Color(0x33FFFFFF)
private val GlassBorder     = Color(0x55FFFFFF)


@Composable
fun HomeScreen(
    onListClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onMicClick: () -> Unit = {}
) {
    var isListening by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "tf_anim")

    val blobOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "blobFloat"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = if (isListening) 1.18f else 1.04f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue  = if (isListening) 0.7f else 0.35f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w3"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(BgMid, BgDeep, Color(0xFFC4D4E0)),
                    start  = Offset(0f, 0f),
                    end    = Offset(1080f, 1920f)
                )
            )
    ) {

        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(
                    x = (-60).dp + (blobOffset * 14).dp,
                    y = 180.dp + (blobOffset * 10).dp
                )
                .blur(48.dp)
                .background(
                    TealSoft.copy(alpha = 0.35f),
                    RoundedCornerShape(50)
                )
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(
                    x = 180.dp - (blobOffset * 10).dp,
                    y = 140.dp - (blobOffset * 8).dp
                )
                .blur(40.dp)
                .background(
                    TealBright.copy(alpha = 0.30f),
                    RoundedCornerShape(50)
                )
        )
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 80.dp)
                .blur(60.dp)
                .background(
                    NavyDark.copy(alpha = 0.18f),
                    CircleShape
                )
        )

        GlassIconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 52.dp),
            onClick = onListClick,
            contentDescription = "Open list"
        ) {
            Icon(
                Icons.Outlined.FormatListBulleted,
                contentDescription = null,
                tint     = NavyDark,
                modifier = Modifier.size(22.dp)
            )
        }

        GlassIconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 52.dp),
            onClick = onSettingsClick,
            contentDescription = "Settings"
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
                tint     = NavyDark,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text     = "Thoughtflow",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            style = TextStyle(
                fontSize   = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color      = NavyDark,
                letterSpacing = 0.5.sp,
                shadow = Shadow(
                    color  = Color.White.copy(alpha = 0.6f),
                    offset = Offset(0f, 1f),
                    blurRadius = 4f
                )
            )
        )

        Column(
            modifier          = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Outer pale ring
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(pulseScale * 1.06f)
                        .background(
                            TealPale.copy(alpha = pulseAlpha * 0.5f),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(172.dp)
                        .scale(pulseScale)
                        .background(
                            TealSoft.copy(alpha = pulseAlpha * 0.65f),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(132.dp)
                        .clip(CircleShape)
                        .background(GlassWhite)
                        .border(1.5.dp, GlassBorder, CircleShape)
                )

                // Mic button
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NavyMid, NavyDark),
                                start  = Offset(0f, 0f),
                                end    = Offset(92f, 92f)
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) {
                            isListening = !isListening
                            onMicClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Mic,
                        contentDescription = if (isListening) "Stop" else "Speak",
                        tint     = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            if (isListening) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    WaveBar(fraction = wave1, color = TealBright)
                    WaveBar(fraction = wave2, color = TealSoft,   maxHeight = 28.dp)
                    WaveBar(fraction = wave3, color = TealBright, maxHeight = 36.dp)
                    WaveBar(fraction = wave2, color = TealSoft,   maxHeight = 28.dp)
                    WaveBar(fraction = wave1, color = TealBright)
                }
            } else {
                Text(
                    text  = "Tap to speak",
                    style = TextStyle(
                        fontSize  = 14.sp,
                        color     = NavyDark.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .clip(RoundedCornerShape(50))
                .background(GlassWhite)
                .border(1.dp, GlassBorder, RoundedCornerShape(50))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text      = if (isListening) "Listening…" else "Your thoughts, captured",
                textAlign = TextAlign.Center,
                style     = TextStyle(
                    fontSize   = 13.sp,
                    color      = NavyDark.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.3.sp
                )
            )
        }
    }
}
