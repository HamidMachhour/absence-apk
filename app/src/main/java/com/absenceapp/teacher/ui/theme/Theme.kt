package com.absenceapp.teacher.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Manech look & feel colour tokens. Naming follows the Manech spec: [ManechSurface]
 * is white (surfaces / text on green headers), [ManechTitle] is orange (title bar /
 * secondary text) and [ManechAccent] is green (card headers, borders, icons, primary text).
 */
val ManechSurface = Color(0xFFFFFFFF)
val ManechTitle = Color(0xFFFF6600)
val ManechAccent = Color(0xFF008000)

/** Shared card/tile corner radii used across Manech-styled screens. */
val ManechCardShape = RoundedCornerShape(4.dp)
val ManechTileShape = RoundedCornerShape(5.dp)
val ManechPanelShape = RoundedCornerShape(10.dp)

private val LightColors = lightColorScheme(
    primary = ManechTitle,
    onPrimary = ManechSurface,
    secondary = ManechAccent,
    onSecondary = ManechSurface,
    background = ManechSurface,
    onBackground = ManechAccent,
    surface = ManechSurface,
    onSurface = ManechAccent,
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF90CAF9),
    secondary = androidx.compose.ui.graphics.Color(0xFF42A5F5),
)

/** Outline/label/cursor colours (green) for text fields, reused across every screen. */
@Composable
fun manechTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ManechAccent,
    unfocusedBorderColor = ManechAccent,
    focusedLabelColor = ManechAccent,
    unfocusedLabelColor = ManechAccent,
    cursorColor = ManechAccent,
)

/** Switch tint (green when checked) reused across every screen. */
@Composable
fun manechSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = ManechSurface,
    checkedTrackColor = ManechAccent,
    checkedBorderColor = ManechAccent,
    uncheckedThumbColor = ManechTitle,
)

@Composable
fun AbsenceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content,
    )
}
