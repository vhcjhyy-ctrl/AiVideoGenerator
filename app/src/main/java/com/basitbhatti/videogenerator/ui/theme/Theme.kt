package com.basitbhatti.videogenerator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EgyptianColorScheme = darkColorScheme(
    primary = Color(0xFFD6AE55),
    onPrimary = Color(0xFF101B1B),
    secondary = Color(0xFFB99A5B),
    tertiary = Color(0xFF6D927B),
    background = Color(0xFF101B1B),
    onBackground = Color(0xFFF4E8CE),
    surface = Color(0xFF1C2A28),
    onSurface = Color(0xFFF4E8CE),
    surfaceVariant = Color(0xFF293936),
    onSurfaceVariant = Color(0xFFC7BFAE)
)

@Composable
fun AiVideoGeneratorTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EgyptianColorScheme,
        typography = Typography,
        content = content
    )
}