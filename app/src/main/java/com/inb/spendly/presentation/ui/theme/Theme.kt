package com.inb.spendly.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceContainer = SurfaceElevatedDark,
    onPrimary = TextPrimaryDark,
    onSurface = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceContainer = SurfaceElevatedLight,
    onPrimary = TextPrimaryLight,
    onSurface = TextSecondaryLight
)

@Composable
fun SpendlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}