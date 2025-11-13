package com.finora.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Esquema de Cores DARK - Elegante e Legível
 * Fundo escuro neutro com Deep Blue e Gold como acentos
 */
private val DarkColorScheme = darkColorScheme(
    // Primary - Deep Blue vibrante para contraste
    primary = Color(0xFF5C6BC0),  // Azul mais claro e vibrante
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF3949AB),
    onPrimaryContainer = Color(0xFFFFFFFF),
    
    // Secondary - Gold brilhante
    secondary = Color(0xFFFFD700),
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF424242),
    onSecondaryContainer = Color(0xFFFFE082),
    
    // Tertiary - Verde esmeralda suave
    tertiary = Color(0xFF66BB6A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF424242),
    onTertiaryContainer = Color(0xFF81C784),
    
    // Background & Surface - Cinza escuro neutro (NÃO Deep Blue)
    background = Color(0xFF121212),  // Cinza escuro padrão Material
    onBackground = Color(0xFFE0E0E0),  // Texto claro
    surface = Color(0xFF1E1E1E),  // Cards cinza
    onSurface = Color(0xFFE0E0E0),  // Texto claro
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0),
    
    // Inversed
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = DeepBlue,
    
    // Error
    error = Color(0xFFEF5350),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = Color(0xFFFFCDD2),
    
    // Outline & Borders
    outline = Color(0xFF424242),
    outlineVariant = Color(0xFF2C2C2C),
    scrim = Color(0x80000000)
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
