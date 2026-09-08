package com.piyush.thoughtflow.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.piyush.thoughtflow.ui.R

// Shared brand accents (identical in both themes)
val PurplePrimary = Color(0xFF6C5CE7)
val PurpleDeep = Color(0xFF5B3FE8)
val PurpleSoft = Color(0xFF9B87FF)
val BlueElectric = Color(0xFF4F8CFF)
val BlueCyan = Color(0xFF6EC8FF)
val PinkAccent = Color(0xFFE86BFF)
val SuccessGreen = Color(0xFF3DDC97)
val DangerRed = Color(0xFFFF5C7A)

// Night palette tokens
private val NightBackground = Color(0xFF0D0D12)
private val NightSurface = Color(0xFF16161F)
private val NightSurfaceElevated = Color(0xFF1E1E2A)
private val NightCard = Color(0xFF1A1A24)
private val NightBorder = Color(0x33FFFFFF)
private val NightBorderStrong = Color(0x55FFFFFF)
private val NightTextPrimary = Color(0xFFF5F5FA)
private val NightTextSecondary = Color(0xFFB0B0C0)
private val NightTextMuted = Color(0xFF7A7A8C)
private val NightGlassBackground = Color(0x22FFFFFF)
private val NightGlassBorder = Color(0x44FFFFFF)

// Day palette tokens (final product design)
private val DayBackground = Color(0xFFF5F6FA)
private val DaySurface = Color(0xFFFFFFFF)
private val DaySurfaceElevated = Color(0xFFF8F9FC)
private val DayCard = Color(0xFFFFFFFF)
private val DayBorder = Color(0xFFE8EAEF)
private val DayBorderStrong = Color(0xFFD1D5E4)
private val DayTextPrimary = Color(0xFF1C1C28)
private val DayTextSecondary = Color(0xFF6B7080)
private val DayTextMuted = Color(0xFF9CA3AF)
private val DayGlassBackground = Color(0xFFFFFFFF)
private val DayGlassBorder = Color(0xFFE8EAEF)

/** Legacy aliases — prefer [ThoughtFlowTheme.colors]. */
val CosmicBlack = NightBackground
val CosmicSurface = NightSurface
val CosmicSurfaceElevated = NightSurfaceElevated
val CosmicCard = NightCard
val CosmicBorder = NightBorder
val CosmicBorderStrong = NightBorderStrong
val TextPrimary = NightTextPrimary
val TextSecondary = NightTextSecondary
val TextMuted = NightTextMuted
val GlassWhite = NightGlassBackground
val GlassBorder = NightGlassBorder
val BgDeep = NightBackground
val BgMid = NightSurface
val NavyDark = NightTextPrimary
val NavyMid = PurplePrimary
val TealBright = PurplePrimary
val TealSoft = PurpleSoft
val TealPale = BlueCyan

data class ThoughtFlowColors(
    val isLight: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val card: Color,
    val border: Color,
    val borderStrong: Color,
    val primary: Color,
    val primaryDeep: Color,
    val accentBlue: Color,
    val accentPink: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val success: Color,
    val danger: Color,
    val glassBackground: Color,
    val glassBorder: Color,
    val brandGradient: Brush,
    val screenGradient: Brush,
    val glowPurple: Color,
    val glowBlue: Color,
    val glowPink: Color,
    val glowCyan: Color,
    val scrimOverlay: Color,
    val disabledButtonGradient: Brush,
    val shadowTint: Color,
    val bottomBarBackground: Color,
    val cardAlpha: Float,
    val useCardShadow: Boolean,
)

fun lightThoughtFlowColors(): ThoughtFlowColors = ThoughtFlowColors(
    isLight = true,
    background = DayBackground,
    surface = DaySurface,
    surfaceElevated = DaySurfaceElevated,
    card = DayCard,
    border = DayBorder,
    borderStrong = DayBorderStrong,
    primary = PurplePrimary,
    primaryDeep = PurpleDeep,
    accentBlue = BlueElectric,
    accentPink = PinkAccent,
    textPrimary = DayTextPrimary,
    textSecondary = DayTextSecondary,
    textMuted = DayTextMuted,
    success = SuccessGreen,
    danger = DangerRed,
    glassBackground = DayGlassBackground,
    glassBorder = DayGlassBorder,
    brandGradient = Brush.horizontalGradient(listOf(PurpleDeep, PurplePrimary, BlueElectric)),
    screenGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8F7FF),
            DayBackground,
            Color(0xFFF0F2F8),
        ),
    ),
    glowPurple = PurplePrimary.copy(alpha = 0.14f),
    glowBlue = BlueElectric.copy(alpha = 0.10f),
    glowPink = PinkAccent.copy(alpha = 0.08f),
    glowCyan = BlueCyan.copy(alpha = 0.08f),
    scrimOverlay = DayBackground.copy(alpha = 0.35f),
    disabledButtonGradient = Brush.horizontalGradient(listOf(Color(0xFFD1D5DB), Color(0xFFD1D5DB))),
    shadowTint = Color(0x1A6C5CE7),
    bottomBarBackground = DaySurface,
    cardAlpha = 1f,
    useCardShadow = true,
)

fun darkThoughtFlowColors(): ThoughtFlowColors = ThoughtFlowColors(
    isLight = false,
    background = NightBackground,
    surface = NightSurface,
    surfaceElevated = NightSurfaceElevated,
    card = NightCard,
    border = NightBorder,
    borderStrong = NightBorderStrong,
    primary = PurplePrimary,
    primaryDeep = PurpleDeep,
    accentBlue = BlueElectric,
    accentPink = PinkAccent,
    textPrimary = NightTextPrimary,
    textSecondary = NightTextSecondary,
    textMuted = NightTextMuted,
    success = SuccessGreen,
    danger = DangerRed,
    glassBackground = NightGlassBackground,
    glassBorder = NightGlassBorder,
    brandGradient = Brush.horizontalGradient(listOf(PurpleDeep, PurplePrimary, BlueElectric)),
    screenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF12121C), NightBackground, Color(0xFF0A0A10)),
    ),
    glowPurple = PurplePrimary.copy(alpha = 0.28f),
    glowBlue = BlueElectric.copy(alpha = 0.22f),
    glowPink = PinkAccent.copy(alpha = 0.12f),
    glowCyan = BlueCyan.copy(alpha = 0.14f),
    scrimOverlay = NightBackground.copy(alpha = 0.65f),
    disabledButtonGradient = Brush.horizontalGradient(listOf(Color(0xFF3A3A48), Color(0xFF3A3A48))),
    shadowTint = PurplePrimary.copy(alpha = 0.45f),
    bottomBarBackground = NightSurface,
    cardAlpha = 0.92f,
    useCardShadow = false,
)

val BrandGradient = Brush.horizontalGradient(
    colors = listOf(PurpleDeep, PurplePrimary, BlueElectric),
)

val BrandGradientVertical = Brush.verticalGradient(
    colors = listOf(PurplePrimary, BlueElectric),
)

val ScreenGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF12121C), NightBackground, Color(0xFF0A0A10)),
)

val LocalThoughtFlowColors = staticCompositionLocalOf { lightThoughtFlowColors() }

object ThoughtFlowTheme {
    val colors: ThoughtFlowColors
        @Composable
        @ReadOnlyComposable
        get() = LocalThoughtFlowColors.current
}

private val SoraFamily = FontFamily(
    Font(R.font.sora_regular, FontWeight.Normal),
    Font(R.font.sora_medium, FontWeight.Medium),
    Font(R.font.sora_semibold, FontWeight.SemiBold),
    Font(R.font.sora_bold, FontWeight.Bold),
)

private fun typography(colors: ThoughtFlowColors) = Typography(
    displayLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        color = colors.textPrimary,
    ),
    headlineLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = colors.textPrimary,
    ),
    headlineMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = colors.textPrimary,
    ),
    titleLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = colors.textPrimary,
    ),
    titleMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = colors.textPrimary,
    ),
    bodyLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = colors.textPrimary,
    ),
    bodyMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = colors.textSecondary,
    ),
    bodySmall = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = colors.textMuted,
    ),
    labelLarge = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        color = colors.textPrimary,
    ),
    labelMedium = TextStyle(
        fontFamily = SoraFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = colors.textSecondary,
    ),
)

@Composable
fun ThoughtFlowTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) darkThoughtFlowColors() else lightThoughtFlowColors()
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = Color.White,
            secondary = colors.accentBlue,
            onSecondary = Color.White,
            tertiary = colors.accentPink,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceElevated,
            onSurfaceVariant = colors.textSecondary,
            outline = colors.borderStrong,
            error = colors.danger,
            onError = Color.White,
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = Color.White,
            secondary = colors.accentBlue,
            onSecondary = Color.White,
            tertiary = colors.accentPink,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceElevated,
            onSurfaceVariant = colors.textSecondary,
            outline = colors.borderStrong,
            error = colors.danger,
            onError = Color.White,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            window.navigationBarColor = colors.bottomBarBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = colors.isLight
                isAppearanceLightNavigationBars = colors.isLight
            }
        }
    }

    CompositionLocalProvider(LocalThoughtFlowColors provides colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography(colors),
            content = content,
        )
    }
}
