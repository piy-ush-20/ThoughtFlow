package com.piyush.thoughtflow.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme

@Composable
fun CosmicBackground(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = ThoughtFlowTheme.colors
    val infinite = rememberInfiniteTransition(label = "cosmic")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "drift",
    )
    val pulse by infinite.animateFloat(
        initialValue = if (colors.isLight) 0.55f else 0.35f,
        targetValue = if (colors.isLight) 0.75f else 0.55f,
        animationSpec = infiniteRepeatable(tween(3200), RepeatMode.Reverse),
        label = "pulse",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.screenGradient),
    ) {
        GlowOrb(
            size = 320.dp,
            x = (-90).dp + if (animated) (drift * 18).dp else 0.dp,
            y = 100.dp + if (animated) (drift * 12).dp else 0.dp,
            color = colors.glowPurple.copy(alpha = if (animated) pulse else colors.glowPurple.alpha),
        )
        GlowOrb(
            size = 280.dp,
            x = 180.dp - if (animated) (drift * 14).dp else 0.dp,
            y = 40.dp - if (animated) (drift * 10).dp else 0.dp,
            color = colors.glowBlue,
        )
        GlowOrb(
            size = 240.dp,
            x = 20.dp,
            y = 520.dp,
            color = colors.glowPink,
        )
        GlowOrb(
            size = 200.dp,
            x = 240.dp,
            y = 640.dp,
            color = colors.glowCyan,
        )
        if (!colors.isLight) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color.Transparent, colors.scrimOverlay),
                            radius = 1200f,
                        ),
                    ),
            )
        }
        content()
    }
}

@Composable
private fun GlowOrb(
    size: androidx.compose.ui.unit.Dp,
    x: androidx.compose.ui.unit.Dp,
    y: androidx.compose.ui.unit.Dp,
    color: Color,
) {
    Box(
        modifier = Modifier
            .size(size)
            .offset(x = x, y = y)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(color, color.copy(alpha = 0f)),
                ),
                shape = CircleShape,
            ),
    )
}
