package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LavenderAccent,
    onPrimary = DeepPurpleOnPrimary,
    primaryContainer = ContainerVariant,
    onPrimaryContainer = TextPrimary,
    secondary = MagentaGlow,
    onSecondary = DeepPurpleOnPrimary,
    secondaryContainer = ContainerVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = CrimsonFire,
    background = DeepMidnight,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderGlass
)

@Composable
fun AuraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

