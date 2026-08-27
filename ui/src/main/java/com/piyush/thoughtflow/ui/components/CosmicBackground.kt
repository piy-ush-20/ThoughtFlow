package com.piyush.thoughtflow.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.piyush.thoughtflow.ui.theme.BlueCyan
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.CosmicBlack
import com.piyush.thoughtflow.ui.theme.PinkAccent
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.ScreenGradient

@Composable
fun CosmicBackground(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val infinite = rememberInfiniteTransition(label = "cosmic")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "drift",
    )
    val pulse by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(tween(3200), RepeatMode.Reverse),
        label = "pulse",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenGradient),
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(
                    x = (-70).dp + if (animated) (drift * 18).dp else 0.dp,
                    y = 120.dp + if (animated) (drift * 12).dp else 0.dp,
                )
                .blur(70.dp)
                .background(PurplePrimary.copy(alpha = if (animated) pulse * 0.45f else 0.28f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(
                    x = 200.dp - if (animated) (drift * 14).dp else 0.dp,
                    y = 80.dp - if (animated) (drift * 10).dp else 0.dp,
                )
                .blur(60.dp)
                .background(BlueElectric.copy(alpha = 0.28f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 40.dp, y = 520.dp)
                .blur(80.dp)
                .background(PinkAccent.copy(alpha = 0.16f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = 260.dp, y = 640.dp)
                .blur(50.dp)
                .background(BlueCyan.copy(alpha = 0.18f), CircleShape),
        )
        // Soft vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, CosmicBlack.copy(alpha = 0.55f)),
                        radius = 1200f,
                    ),
                ),
        )
        content()
    }
}
