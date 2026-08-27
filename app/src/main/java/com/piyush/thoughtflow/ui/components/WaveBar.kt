package com.piyush.thoughtflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaveBar(
    fraction: Float,
    color: Color,
    maxHeight: androidx.compose.ui.unit.Dp = 20.dp
) {
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(maxHeight * fraction)
            .clip(RoundedCornerShape(50))
            .background(color)
    )
}