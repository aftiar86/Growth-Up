package com.growthup.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = GrowthUpColors.WarmRose,
    onPrimary = GrowthUpColors.SurfaceWhite,
    primaryContainer = GrowthUpColors.SoftBlush,
    onPrimaryContainer = GrowthUpColors.TextCharcoal,
    secondary = GrowthUpColors.SoftBlush,
    onSecondary = GrowthUpColors.TextCharcoal,
    background = GrowthUpColors.BackgroundCream,
    onBackground = GrowthUpColors.TextCharcoal,
    surface = GrowthUpColors.SurfaceWhite,
    onSurface = GrowthUpColors.TextCharcoal,
    surfaceVariant = GrowthUpColors.BackgroundCream,
    onSurfaceVariant = GrowthUpColors.TextMuted,
    outline = GrowthUpColors.CardBorder
)

@Composable
fun GrowthUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Growth Up uses the refined soft-pink minimalist aesthetic
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
