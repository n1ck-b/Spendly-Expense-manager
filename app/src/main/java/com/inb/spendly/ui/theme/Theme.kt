package com.inb.spendly.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6D7D9E),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color(0xFF1D1A23),
    onPrimary = Color(0xFFFCFBFC),
    onBackground = Color(0xFFFCFBFC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF49556E),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    //Other default colors to override
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF7F7FE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun SpendlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}