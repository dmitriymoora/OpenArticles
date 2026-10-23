package com.example.openarticles.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2B6C8F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC7E7FF),
    onPrimaryContainer = Color(0xFF001E2E),
    secondary = Color(0xFF4F616E),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF6FAFE),
    onBackground = Color(0xFF171C20),
    surface = Color(0xFFF6FAFE),
    onSurface = Color(0xFF171C20),
    surfaceVariant = Color(0xFFDDE3EA),
    onSurfaceVariant = Color(0xFF41484D),
    surfaceContainer = Color(0xFFEAEEF2),
    outline = Color(0xFF71787E),
    error = Color(0xFFBA1A1A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8ACEF7),
    onPrimary = Color(0xFF00344C),
    primaryContainer = Color(0xFF004C6C),
    onPrimaryContainer = Color(0xFFC7E7FF),
    secondary = Color(0xFFB6C9D8),
    onSecondary = Color(0xFF21323E),
    background = Color(0xFF0F1417),
    onBackground = Color(0xFFDEE3E7),
    surface = Color(0xFF0F1417),
    onSurface = Color(0xFFDEE3E7),
    surfaceVariant = Color(0xFF41484D),
    onSurfaceVariant = Color(0xFFC1C7CE),
    surfaceContainer = Color(0xFF1B2024),
    outline = Color(0xFF8B9198),
    error = Color(0xFFFFB4AB),
)

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
