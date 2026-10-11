package com.bugra.tabu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = OnAccent,
    background = DarkBg,
    onBackground = TextMain,
    surface = DarkSurface1,
    onSurface = TextMain,
    surfaceVariant = DarkSurface2,
    onSurfaceVariant = TextSub
)

@Composable
fun TabuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
