package com.piyush.thoughtflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.piyush.thoughtflow.ui.R

/** Deep near-black navy used as the primary canvas. */
val CosmicBlack = Color(0xFF0D0D12)
val CosmicSurface = Color(0xFF16161F)
val CosmicSurfaceElevated = Color(0xFF1E1E2A)
val CosmicCard = Color(0xFF1A1A24)
val CosmicBorder = Color(0x33FFFFFF)
val CosmicBorderStrong = Color(0x55FFFFFF)

val PurplePrimary = Color(0xFF7B61FF)
val PurpleDeep = Color(0xFF5B3FE8)
val PurpleSoft = Color(0xFF9B87FF)
val BlueElectric = Color(0xFF4F8CFF)
val BlueCyan = Color(0xFF6EC8FF)
val PinkAccent = Color(0xFFE86BFF)

val TextPrimary = Color(0xFFF5F5FA)
val TextSecondary = Color(0xFFB0B0C0)
val TextMuted = Color(0xFF7A7A8C)

val SuccessGreen = Color(0xFF3DDC97)
val DangerRed = Color(0xFFFF5C7A)

val GlassWhite = Color(0x22FFFFFF)
val GlassBorder = Color(0x44FFFFFF)

/** Legacy aliases kept so older call sites compile during migration. */
val BgDeep = CosmicBlack
val BgMid = CosmicSurface
val NavyDark = TextPrimary
val NavyMid = PurplePrimary
val TealBright = PurplePrimary
val TealSoft = PurpleSoft
val TealPale = BlueCyan

val BrandGradient = Brush.horizontalGradient(
    colors = listOf(PurpleDeep, PurplePrimary, BlueElectric),
)

val BrandGradientVertical = Brush.verticalGradient(
    colors = listOf(PurplePrimary, BlueElectric),
)

val ScreenGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF12121C),
        CosmicBlack,
        Color(0xFF0A0A10),
    ),
)

data class ThoughtFlowColors(
    val background: Color = CosmicBlack,
    val surface: Color = CosmicSurface,
    val surfaceElevated: Color = CosmicSurfaceElevated,
    val card: Color = CosmicCard,
    val border: Color = CosmicBorder,
    val primary: Color = PurplePrimary,
    val primaryDeep: Color = PurpleDeep,
    val accentBlue: Color = BlueElectric,
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val textMuted: Color = TextMuted,
    val success: Color = SuccessGreen,
    val danger: Color = DangerRed,
    val brandGradient: Brush = BrandGradient,
    val screenGradient: Brush = ScreenGradient,
)

val LocalThoughtFlowColors = staticCompositionLocalOf { ThoughtFlowColors() }

private val SoraFamily = FontFamily(
    Font(R.font.sora_regular, FontWeight.Normal),
    Font(R.font.sora_medium, FontWeight.Medium),
    Font(R.font.sora_semibold, FontWeight.SemiBold),
    Font(R.font.sora_bold, FontWeight.Bold),
)

private val ThoughtFlowTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        color = TextPrimary,
    ),
    headlineLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = TextPrimary,
    ),
    headlineMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = TextPrimary,
    ),
    titleLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = TextPrimary,
    ),
    titleMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = TextPrimary,
    ),
    bodyLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = TextPrimary,
    ),
    bodyMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = TextSecondary,
    ),
    bodySmall = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = TextMuted,
    ),
    labelLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        color = TextPrimary,
    ),
    labelMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = TextSecondary,
    ),
)

private val ThoughtFlowColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    secondary = BlueElectric,
    onSecondary = Color.White,
    tertiary = PinkAccent,
    background = CosmicBlack,
    onBackground = TextPrimary,
    surface = CosmicSurface,
    onSurface = TextPrimary,
    surfaceVariant = CosmicSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = CosmicBorderStrong,
    error = DangerRed,
    onError = Color.White,
)

@Composable
fun ThoughtFlowTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalThoughtFlowColors provides ThoughtFlowColors()) {
        MaterialTheme(
            colorScheme = ThoughtFlowColorScheme,
            typography = ThoughtFlowTypography,
            content = content,
        )
    }
}
