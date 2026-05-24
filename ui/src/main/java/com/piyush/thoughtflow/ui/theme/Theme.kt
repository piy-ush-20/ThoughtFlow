package com.piyush.thoughtflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BgDeep = Color(0xFFB8C8D8)
val BgMid = Color(0xFFD0DDE8)
val NavyDark = Color(0xFF2C3E55)
val NavyMid = Color(0xFF3D5068)
val TealBright = Color(0xFF4BA8A8)
val TealSoft = Color(0xFF8DCACB)
val TealPale = Color(0xFFB4DDE0)
val GlassWhite = Color(0x33FFFFFF)
val GlassBorder = Color(0x55FFFFFF)

private val ThoughtFlowColorScheme = lightColorScheme(
    primary = NavyDark,
    onPrimary = Color.White,
    secondary = TealBright,
    onSecondary = Color.White,
    background = BgMid,
    onBackground = NavyDark,
    surface = Color.White.copy(alpha = 0.85f),
    onSurface = NavyDark,
)

@Composable
fun ThoughtFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ThoughtFlowColorScheme,
        content = content,
    )
}
