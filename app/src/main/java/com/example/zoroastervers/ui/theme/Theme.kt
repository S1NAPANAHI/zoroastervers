package com.example.zoroastervers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary Brand Colors
val ZoroasterPrimary = Color(0xFF2E3440)
val ZoroasterPrimaryVariant = Color(0xFF3B4252)
val ZoroasterSecondary = Color(0xFF5E81AC)
val ZoroasterTertiary = Color(0xFF88C0D0)

// Reading Theme Colors
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFF8F9FA)
val LightOnBackground = Color(0xFF2E3440)

val DarkBackground = Color(0xFF1C1C1E)
val DarkSurface = Color(0xFF2C2C2E)
val DarkOnBackground = Color(0xFFE5E5E7)

val SepiaBackground = Color(0xFFF4ECD8)
val SepiaSurface = Color(0xFFEEE0C9)
val SepiaOnBackground = Color(0xFF5C4B37)

// Light Color Scheme
private val LightColors = lightColorScheme(
    primary = ZoroasterPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1E8ED),
    onPrimaryContainer = ZoroasterPrimary,
    
    secondary = ZoroasterSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8E6F3),
    onSecondaryContainer = ZoroasterSecondary,
    
    tertiary = ZoroasterTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE8F4F8),
    onTertiaryContainer = ZoroasterTertiary,
    
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnBackground,
    
    surfaceVariant = Color(0xFFF1F3F4),
    onSurfaceVariant = Color(0xFF5F6368),
    
    outline = Color(0xFFDADCE0),
    outlineVariant = Color(0xFFE8EAED)
)

// Dark Color Scheme
private val DarkColors = darkColorScheme(
    primary = ZoroasterTertiary,
    onPrimary = ZoroasterPrimary,
    primaryContainer = Color(0xFF4C566A),
    onPrimaryContainer = Color(0xFFD8DEE9),
    
    secondary = Color(0xFF81A1C1),
    onSecondary = ZoroasterPrimary,
    secondaryContainer = Color(0xFF434C5E),
    onSecondaryContainer = Color(0xFFD8DEE9),
    
    tertiary = Color(0xFF8FBCBB),
    onTertiary = ZoroasterPrimary,
    tertiaryContainer = Color(0xFF434C5E),
    onTertiaryContainer = Color(0xFFD8DEE9),
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnBackground,
    
    surfaceVariant = Color(0xFF3A3A3C),
    onSurfaceVariant = Color(0xFFAEAEB2),
    
    outline = Color(0xFF48484A),
    outlineVariant = Color(0xFF3A3A3C)
)

// Reading Themes
enum class ReadingTheme {
    LIGHT, DARK, SEPIA
}

@Composable
fun ZoroasterVersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    readingTheme: ReadingTheme? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        readingTheme == ReadingTheme.SEPIA -> lightColorScheme(
            primary = Color(0xFF8B4513),
            onPrimary = Color.White,
            background = SepiaBackground,
            onBackground = SepiaOnBackground,
            surface = SepiaSurface,
            onSurface = SepiaOnBackground
        )
        readingTheme == ReadingTheme.DARK -> DarkColors
        readingTheme == ReadingTheme.LIGHT -> LightColors
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}