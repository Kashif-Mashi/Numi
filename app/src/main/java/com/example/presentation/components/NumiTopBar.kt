package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts

@Composable
fun NumiTopBar(
    isScientificMode: Boolean,
    onToggleScientific: () -> Unit,
    onOpenThemes: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brand badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(theme.accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NUMI",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                ),
                color = theme.primaryText
            )
        }

        // Tactile action pills
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBarActionPill(
                icon = Icons.Rounded.Functions,
                contentDescription = stringResource(R.string.cd_toggle_scientific),
                testTag = "top_bar_scientific",
                active = isScientificMode,
                onClick = onToggleScientific
            )
            TopBarActionPill(
                icon = Icons.Outlined.Palette,
                contentDescription = stringResource(R.string.cd_open_themes),
                testTag = "top_bar_themes",
                active = false,
                onClick = onOpenThemes,
                showAccentDot = true
            )
            TopBarActionPill(
                icon = Icons.Outlined.History,
                contentDescription = stringResource(R.string.cd_open_history),
                testTag = "top_bar_history",
                active = false,
                onClick = onOpenHistory
            )
            TopBarActionPill(
                icon = Icons.Outlined.Tune,
                contentDescription = stringResource(R.string.cd_open_settings),
                testTag = "top_bar_settings",
                active = false,
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
private fun TopBarActionPill(
    icon: ImageVector,
    contentDescription: String,
    testTag: String,
    active: Boolean,
    onClick: () -> Unit,
    showAccentDot: Boolean = false
) {
    val theme = LocalNumiTheme.current
    val bg by animateColorAsState(
        targetValue = if (active) theme.accentColor else theme.displaySurface.copy(alpha = 0.9f),
        label = "pillBg"
    )
    val fg by animateColorAsState(
        targetValue = if (active) theme.operatorKeyText else theme.primaryText,
        label = "pillFg"
    )

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(1.dp, if (active) theme.accentColor else theme.displayBorder, RoundedCornerShape(16.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = fg,
            modifier = Modifier.size(22.dp)
        )
        if (showAccentDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(theme.accentColor)
            )
        }
    }
}
