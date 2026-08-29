package com.piyush.thoughtflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.PurplePrimary

@Composable
fun WaveBar(
    fraction: Float,
    color: Color = PurplePrimary,
    maxHeight: Dp = 20.dp,
) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(maxHeight * fraction.coerceIn(0.15f, 1f))
            .clip(RoundedCornerShape(50))
            .background(
                Brush.verticalGradient(listOf(color, BlueElectric.copy(alpha = 0.85f))),
            ),
    )
}
