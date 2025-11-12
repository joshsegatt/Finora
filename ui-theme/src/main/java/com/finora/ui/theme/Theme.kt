package com.finora.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de Cores DARK - Deep Blue + Gold
 * Fundo Deep Blue profundo com acentos dourados
 */
private val DarkColorScheme = darkColorScheme(
    // Primary - Deep Blue
    primary = DeepBlue,
    onPrimary = DarkTextPrimary,
    primaryContainer = DarkBlue,
    onPrimaryContainer = AccentGoldDark,
    
    // Secondary - Gold para destaque
    secondary = AccentGoldDark,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    
    // Tertiary - Verde esmeralda
    tertiary = AccentEmeraldDark,
    onTertiary = DarkTextPrimary,
    tertiaryContainer = DarkSurfaceVariant,
    onTertiaryContainer = DarkTextPrimary,
    
    // Background & Surface
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    
    // Inversed
    inverseSurface = LightSurface,
    inverseOnSurface = LightTextPrimary,
    inversePrimary = DeepBlue,
    
    // Error
    error = ErrorDark,
    onError = DarkTextPrimary,
    errorContainer = ErrorDark,
    onErrorContainer = DarkTextPrimary,
    
    // Outline & Borders
    outline = BorderDark,
    outlineVariant = DarkSurfaceVariant,
    scrim = OverlayDark
)

/**
 * Esquema de Cores LIGHT - Minimalista Luxo
 * Fundo branco puro com Deep Blue e Gold
 */
private val LightColorScheme = lightColorScheme(
    // Primary - Deep Blue
    primary = DeepBlue,
    onPrimary = LightSurface,
    primaryContainer = LightGold,
    onPrimaryContainer = DeepBlue,
    
    // Secondary - Gold para destaque premium
    secondary = Gold,
    onSecondary = DarkBlue,
    secondaryContainer = LightSurfaceVariant,
    onSecondaryContainer = LightTextPrimary,
    
    // Tertiary - Verde esmeralda
    tertiary = AccentEmerald,
    onTertiary = LightSurface,
    tertiaryContainer = LightSurfaceVariant,
    onTertiaryContainer = LightTextPrimary,
    
    // Background & Surface
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    
    // Inversed
    inverseSurface = DarkSurface,
    inverseOnSurface = DarkTextPrimary,
    inversePrimary = AccentGoldDark,
    
    // Error
    error = ErrorLight,
    onError = LightSurface,
    errorContainer = ErrorLight,
    onErrorContainer = LightSurface,
    
    // Outline & Borders
    outline = BorderLight,
    outlineVariant = LightSurfaceVariant,
    scrim = OverlayLight
)

/**
 * Tema Principal do Finora
 * Design minimalista e luxuoso com suporte a dark/light mode
 */
@Composable
fun FinoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
