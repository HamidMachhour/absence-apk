package com.absenceapp.teacher.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absenceapp.teacher.ui.theme.ManechAccent
import com.absenceapp.teacher.ui.theme.ManechCardShape
import com.absenceapp.teacher.ui.theme.ManechSurface
import com.absenceapp.teacher.ui.theme.ManechTileShape
import com.absenceapp.teacher.ui.theme.ManechTitle

/**
 * Manech "section card": a white, elevated, rounded card with a green header strip.
 * This is the single shared helper for the collapsible-card behaviour described in the
 * Manech look & feel spec - every screen reuses it instead of duplicating the toggle logic.
 *
 * When [collapsible] is true the whole header acts as the toggle target and the arrow icon
 * always matches [expanded] (down = collapsed, up = expanded). When false, the header is purely
 * decorative (no arrow, not clickable) and [content] is always shown - used for single-section
 * screens that have nothing to collapse.
 */
@Composable
fun CollapsibleCard(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    expandContentDescription: String,
    collapseContentDescription: String,
    modifier: Modifier = Modifier,
    collapsible: Boolean = true,
    header: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ManechCardShape,
        colors = CardDefaults.cardColors(containerColor = ManechSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ManechAccent)
                    .then(
                        if (collapsible) Modifier.clickable(onClick = onToggleExpanded) else Modifier,
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (collapsible) {
                    Icon(
                        imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = if (expanded) collapseContentDescription else expandContentDescription,
                        tint = ManechSurface,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 8.dp),
                    )
                }
                header()
            }
            if (expanded) {
                Column(modifier = Modifier.padding(4.dp)) {
                    content()
                }
            }
        }
    }
}

/** Orange screen title bar with centered white text, placed above the screen content. */
@Composable
fun ManechTitleBar(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ManechTitle)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = title, color = ManechSurface, fontSize = 20.sp)
    }
}

/**
 * Manech outlined list tile: a green 1dp outline, 5dp rounded corners, a primary (green, 18sp)
 * line and a secondary (orange, 14sp) line, with a trailing slot for actions/controls.
 */
@Composable
fun ManechTile(
    primaryText: String,
    secondaryText: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .border(BorderStroke(1.dp, ManechAccent), ManechTileShape)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = primaryText, color = ManechAccent, fontSize = 18.sp, maxLines = 2)
            Text(text = secondaryText, color = ManechTitle, fontSize = 14.sp, maxLines = 2)
        }
        trailing()
    }
}
