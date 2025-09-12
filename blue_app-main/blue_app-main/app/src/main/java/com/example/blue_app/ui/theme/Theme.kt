package com.example.blue_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),        // Azul primario
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB), // Azul claro
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFF64B5F6),      // Azul secundario
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF0D47A1),
    tertiary = Color(0xFF1E88E5),       // Azul terciario
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFBBDEFB),
    onTertiaryContainer = Color(0xFF0D47A1),
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFE3F2FD),        // Azul claro para tarjetas
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFBBDEFB),
    onSurfaceVariant = Color(0xFF4A4A4A),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0x99000000),
    inverseSurface = Color(0xFF1F1F1F),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFFFF80AB)
)

@Composable
fun Blue_appTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}