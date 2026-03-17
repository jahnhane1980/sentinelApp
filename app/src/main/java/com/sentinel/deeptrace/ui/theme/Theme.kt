package com.sentinel.deeptrace.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// Wir definieren hier nur das LightColorScheme, da du einen weißen Hintergrund wünschst
private val LightColorScheme = lightColorScheme(
    primary = SentinelBlue,
    secondary = SentinelTurquoise,
    tertiary = SentinelOrange,
    background = SentinelBackground,
    surface = SentinelBackground,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = SentinelBlue,
    onSurface = SentinelBlue
)

@Composable
fun SentinelAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        // Statusbar-Farbe oben am Handy anpassen
        window.statusBarColor = colorScheme.background.toArgb()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Falls du hier einen Fehler hast, erstelle eine Standard-Typography oder lösche die Zeile
        content = content
    )
}