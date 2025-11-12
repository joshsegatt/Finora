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
 * Esquema de Cores DARK - Elegante & Luxuoso
 * Fundo preto profundo com acentos dourados e petróleo
 */
private val DarkColorScheme = darkColorScheme(
    // Primary - Dourado para destaque premium
    primary = AccentGoldDark,
    onPrimary = DarkBackground,
    primaryContainer = AccentTealDark,
    onPrimaryContainer = DarkTextPrimary,
    
    // Secondary - Verde esmeralda para ações secundárias
    secondary = AccentEmeraldDark,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    
    // Tertiary - Azul petróleo
    tertiary = AccentTealDark,
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
    inversePrimary = AccentGold,
    
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
 * Esquema de Cores LIGHT - Minimalista & Clean
 * Fundo branco/cinza claro com acentos dourados e petróleo
 */
private val LightColorScheme = lightColorScheme(
    // Primary - Dourado para destaque premium
    primary = AccentGold,
    onPrimary = LightTextPrimary,
    primaryContainer = AccentTeal,
    onPrimaryContainer = LightSurface,
    
    // Secondary - Verde esmeralda para ações secundárias
    secondary = AccentEmerald,
    onSecondary = LightSurface,
    secondaryContainer = LightSurfaceVariant,
    onSecondaryContainer = LightTextPrimary,
    
    // Tertiary - Azul petróleo
    tertiary = AccentTeal,
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
