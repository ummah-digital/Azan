package com.azan.prayertimes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MakkahGold,
    onPrimary = ObsidianBackground,
    primaryContainer = IslamicGreenContainer,
    onPrimaryContainer = MakkahGoldLight,
    secondary = IslamicGreenLight,
    onSecondary = TextPrimary,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = IslamicGreen,
    onPrimary = TextPrimary,
    primaryContainer = MakkahGoldContainer,
    onPrimaryContainer = IslamicGreenDark,
    secondary = MakkahGoldDark,
    onSecondary = ObsidianBackground,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceCardBorder
)

@Composable
fun AzanTheme(
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
