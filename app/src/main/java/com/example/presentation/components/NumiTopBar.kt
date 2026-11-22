package com.example.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
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
import androidx.compose.ui.res.painterResource
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
    onOpenAbout: () -> Unit,
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
        // Brand with original app icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "NUMI Icon",
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(0.5.dp, theme.displayBorder, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
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

        // Action pills: Scientific, Themes, About
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBarActionPill(
                icon = Icons.Rounded.Functions,
                contentDescription = "Toggle Scientific Mode",
                testTag = "top_bar_scientific",
                active = isScientificMode,
                onClick = onToggleScientific
            )
            TopBarActionPill(
                icon = Icons.Outlined.Palette,
                contentDescription = "Open Themes",
                testTag = "top_bar_themes",
                active = false,
                onClick = onOpenThemes
            )
            TopBarActionPill(
                icon = Icons.Outlined.Info,
                contentDescription = "About",
                testTag = "top_bar_about",
                active = false,
                onClick = onOpenAbout
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
    onClick: () -> Unit
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
            .defaultMinSize(minWidth = 46.dp, minHeight = 46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, if (active) theme.accentColor else theme.displayBorder, RoundedCornerShape(14.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = fg,
            modifier = Modifier.size(20.dp)
        )
    }
}
