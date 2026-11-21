package com.example.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.animations.NumiFeedbackManager
import com.example.animations.SoundEffectType
import com.example.domain.models.AngleUnit
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.UserPreferences
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    feedbackManager: NumiFeedbackManager,
    onSetAnimationIntensity: (AnimationIntensity) -> Unit,
    onSetHapticsEnabled: (Boolean) -> Unit,
    onSetSoundEnabled: (Boolean) -> Unit,
    onSetAngleUnit: (AngleUnit) -> Unit,
    onOpenThemes: () -> Unit,
    onResetPreferences: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current
    val view = LocalView.current
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(theme.backgroundTop, theme.backgroundBottom))
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.displaySurface)
                    .border(1.dp, theme.displayBorder, RoundedCornerShape(16.dp))
                    .clickable(role = Role.Button, onClick = onNavigateBack)
                    .testTag("settings_back_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.cd_navigate_back),
                    tint = theme.primaryText
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = theme.primaryText
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Theme shortcut
            item {
                val shape = RoundedCornerShape(22.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, shape)
                        .clickable(role = Role.Button, onClick = onOpenThemes)
                        .padding(16.dp)
                        .testTag("settings_theme_row"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(theme.accentColor.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Palette,
                                contentDescription = null,
                                tint = theme.accentColor
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Theme",
                                style = MaterialTheme.typography.titleMedium,
                                color = theme.primaryText
                            )
                            Text(
                                text = preferences.themeId.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = theme.secondaryText
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = theme.secondaryText
                    )
                }
            }

            // Animation Intensity
            item {
                val shape = RoundedCornerShape(22.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, shape)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = theme.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Animation Intensity",
                            style = MaterialTheme.typography.titleMedium,
                            color = theme.primaryText
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnimationIntensity.entries.forEach { option ->
                            val selected = preferences.animationIntensity == option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (selected) theme.accentColor else theme.utilityKeyBg)
                                    .clickable(role = Role.Button) {
                                        onSetAnimationIntensity(option)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                                    .testTag("intensity_${option.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) theme.operatorKeyText else theme.utilityKeyText
                                )
                            }
                        }
                    }
                    Text(
                        text = preferences.animationIntensity.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.secondaryText
                    )
                }
            }

            // Tactile & Sound feedback toggles
            item {
                val shape = RoundedCornerShape(22.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, shape)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    SettingsToggleRow(
                        icon = Icons.Outlined.Vibration,
                        title = "Haptic Feedback",
                        subtitle = "Crisp tactile vibration on key presses",
                        checked = preferences.hapticsEnabled,
                        testTag = "settings_haptics_switch",
                        onCheckedChange = { enabled ->
                            onSetHapticsEnabled(enabled)
                            if (enabled) {
                                feedbackManager.triggerButtonFeedback(
                                    view = view,
                                    soundEnabled = false,
                                    hapticsEnabled = true,
                                    type = SoundEffectType.DIGIT
                                )
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(theme.displayBorder.copy(alpha = 0.6f))
                    )
                    SettingsToggleRow(
                        icon = Icons.Outlined.VolumeUp,
                        title = "Sound Effects",
                        subtitle = "Subtle marimba key clicks and solve chime",
                        checked = preferences.soundEnabled,
                        testTag = "settings_sound_switch",
                        onCheckedChange = { enabled ->
                            onSetSoundEnabled(enabled)
                            if (enabled) {
                                feedbackManager.triggerButtonFeedback(
                                    view = view,
                                    soundEnabled = true,
                                    hapticsEnabled = false,
                                    type = SoundEffectType.DIGIT
                                )
                            }
                        }
                    )
                }
            }

            // Angle Unit
            item {
                val shape = RoundedCornerShape(22.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, shape)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Trigonometry Unit",
                            style = MaterialTheme.typography.titleMedium,
                            color = theme.primaryText
                        )
                        Text(
                            text = if (preferences.angleUnit == AngleUnit.DEG) "Degrees (sin 30° = 0.5)" else "Radians (sin π = 0)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = theme.secondaryText
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AngleUnit.entries.forEach { unit ->
                            val selected = preferences.angleUnit == unit
                            Box(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 56.dp, minHeight = 48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (selected) theme.accentColor else theme.utilityKeyBg)
                                    .clickable(role = Role.Button) { onSetAngleUnit(unit) }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unit.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) theme.operatorKeyText else theme.utilityKeyText
                                )
                            }
                        }
                    }
                }
            }

            // About NUMI
            item {
                val shape = RoundedCornerShape(22.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(theme.displaySurface)
                        .border(1.dp, theme.displayBorder, shape)
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(theme.accentColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "About NUMI",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = NumiFonts.displayFamily(theme.fontStyle)
                            ),
                            color = theme.primaryText
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_tagline),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                            fontWeight = FontWeight.Bold
                        ),
                        color = theme.accentColor
                    )
                    Text(
                        text = stringResource(R.string.about_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.secondaryText
                    )
                    Text(
                        text = "Version 1.0 • Tip: Drag any display number to rotate it in 3D, or pinch with two fingers.",
                        style = MaterialTheme.typography.labelMedium,
                        color = theme.secondaryText
                    )
                }
            }

            // Reset Preferences
            item {
                val shape = RoundedCornerShape(22.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 56.dp)
                        .clip(shape)
                        .background(theme.utilityKeyBg)
                        .clickable(role = Role.Button) { showResetDialog = true }
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                        .testTag("settings_reset_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RestartAlt,
                        contentDescription = null,
                        tint = theme.utilityKeyText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset Preferences",
                        style = MaterialTheme.typography.labelLarge,
                        color = theme.utilityKeyText
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Preferences?") },
            text = { Text("Restore NUMI to Cream Classic theme, Playful motion, and default feedback settings.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetPreferences()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    val theme = LocalNumiTheme.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = theme.accentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.primaryText
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.secondaryText
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = theme.operatorKeyText,
                checkedTrackColor = theme.accentColor
            )
        )
    }
}
