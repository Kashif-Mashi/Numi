package com.example.presentation.themes

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.NumiFeedbackManager
import com.example.animations.SoundEffectType
import com.example.domain.models.AngleUnit
import com.example.domain.models.NumiThemeSpec
import com.example.domain.models.TextDepthStyle
import com.example.domain.models.TextFontOverride
import com.example.domain.models.TextShadowStyle
import com.example.domain.models.ThemeId
import com.example.presentation.components.Interactive3DNumberDisplay
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.theme.NumiTextCustomization
import com.example.ui.theme.NumiTheme

@Composable
fun ThemesScreen(
    uiState: ThemesUiState,
    feedbackManager: NumiFeedbackManager,
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    onPreviewTheme: (ThemeId) -> Unit,
    onPreviewCustomAccent: (Long) -> Unit,
    onPreviewCustomDark: (Boolean) -> Unit,
    onSetTextFontOverride: (TextFontOverride) -> Unit,
    onSetTextDepthStyle: (TextDepthStyle) -> Unit,
    onSetTextShadowStyle: (TextShadowStyle) -> Unit,
    onSetNumberScaleFactor: (Float) -> Unit,
    onApplyTheme: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTheme = LocalNumiTheme.current
    val preview = uiState.previewSpec
    val isPreviewActive = uiState.previewThemeId == uiState.activeThemeId &&
        (uiState.previewThemeId != ThemeId.CUSTOM)
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current

    val customSwatches = listOf(
        0xFF3B82F6L, // Blue
        0xFF10B981L, // Emerald
        0xFFF59E0BL, // Amber
        0xFFEF4444L, // Crimson
        0xFF8B5CF6L, // Violet
        0xFF06B6D4L  // Cyan
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(activeTheme.backgroundTop, activeTheme.backgroundBottom))
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(activeTheme.displaySurface)
                    .border(1.dp, activeTheme.displayBorder, RoundedCornerShape(14.dp))
                    .clickable(role = Role.Button, onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = activeTheme.primaryText
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Color Themes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = activeTheme.primaryText
                )
                Text(
                    text = "Choose your preferred calculator color palette",
                    style = MaterialTheme.typography.bodySmall,
                    color = activeTheme.secondaryText
                )
            }
        }

        // Live Preview Stage
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            NumiTheme(themeSpec = preview) {
                val pTheme = LocalNumiTheme.current
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(pTheme.backgroundTop, pTheme.backgroundBottom)
                            )
                        )
                        .border(1.dp, pTheme.displayBorder, RoundedCornerShape(22.dp))
                        .padding(14.dp)
                ) {
                    Interactive3DNumberDisplay(
                        expression = "128 + 256",
                        primaryDisplay = "384",
                        livePreview = null,
                        isError = false,
                        justEvaluated = true,
                        pulseTrigger = 0,
                        angleUnit = AngleUnit.DEG,
                        isScientificMode = false,
                        onToggleAngleUnit = {},
                        modifier = Modifier.height(115.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sample mini keycaps
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MiniKeySample("7", pTheme.digitKeyBg, pTheme.digitKeyText)
                            MiniKeySample("+", pTheme.operatorKeyBg, pTheme.operatorKeyText)
                            MiniKeySample("=", pTheme.equalsKeyBg, pTheme.equalsKeyText)
                        }

                        // Apply Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isPreviewActive) pTheme.displaySurface else pTheme.accentColor)
                                .border(1.dp, if (isPreviewActive) pTheme.displayBorder else Color.Transparent, RoundedCornerShape(14.dp))
                                .clickable(role = Role.Button) {
                                    feedbackManager.triggerButtonFeedback(
                                        view = view,
                                        soundEnabled = soundEnabled,
                                        hapticsEnabled = hapticsEnabled,
                                        type = SoundEffectType.EQUALS,
                                        hapticFeedback = haptic
                                    )
                                    onApplyTheme()
                                }
                                .padding(horizontal = 18.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isPreviewActive) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = pTheme.primaryText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = if (isPreviewActive) "Active" else "Apply Theme",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = if (isPreviewActive) pTheme.primaryText else pTheme.operatorKeyText
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Themes List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Custom Color Palette Card
            item(key = "custom_controls") {
                AnimatedVisibility(visible = uiState.previewThemeId == ThemeId.CUSTOM) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(activeTheme.displaySurface)
                            .border(1.dp, activeTheme.displayBorder, RoundedCornerShape(18.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Select Accent Color",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = activeTheme.primaryText
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            customSwatches.forEach { hex ->
                                val selected = uiState.customAccentHex == hex
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex.toInt()))
                                        .border(
                                            width = if (selected) 2.5.dp else 1.dp,
                                            color = if (selected) activeTheme.primaryText else Color(0x33FFFFFF),
                                            shape = CircleShape
                                        )
                                        .clickable { onPreviewCustomAccent(hex) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            BaseToneButton(
                                label = "Light Base",
                                icon = Icons.Rounded.LightMode,
                                selected = !uiState.customIsDark,
                                theme = activeTheme,
                                onClick = { onPreviewCustomDark(false) }
                            )
                            BaseToneButton(
                                label = "Dark Base",
                                icon = Icons.Rounded.DarkMode,
                                selected = uiState.customIsDark,
                                theme = activeTheme,
                                onClick = { onPreviewCustomDark(true) }
                            )
                        }
                    }
                }
            }

            // Theme Cards
            items(uiState.allThemes, key = { it.id.name }) { spec ->
                ThemeCardClean(
                    spec = spec,
                    isActive = spec.id == uiState.activeThemeId,
                    isPreviewed = spec.id == uiState.previewThemeId,
                    activeTheme = activeTheme,
                    onClick = { onPreviewTheme(spec.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun MiniKeySample(
    label: String,
    bg: Color,
    fg: Color
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = fg
        )
    }
}

@Composable
private fun BaseToneButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    theme: NumiThemeSpec,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) theme.accentColor else theme.backgroundTop)
            .border(1.dp, if (selected) theme.accentColor else theme.displayBorder, RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) theme.operatorKeyText else theme.primaryText,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = if (selected) theme.operatorKeyText else theme.primaryText
            )
        }
    }
}

@Composable
private fun ThemeCardClean(
    spec: NumiThemeSpec,
    isActive: Boolean,
    isPreviewed: Boolean,
    activeTheme: NumiThemeSpec,
    onClick: () -> Unit
) {
    val borderColor = when {
        isPreviewed -> activeTheme.accentColor
        isActive -> activeTheme.primaryText.copy(alpha = 0.5f)
        else -> activeTheme.displayBorder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(activeTheme.displaySurface)
            .border(if (isPreviewed) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = spec.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = activeTheme.primaryText
                    )
                    if (isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(activeTheme.accentColor.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = activeTheme.accentColor
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = spec.tagline,
                    style = MaterialTheme.typography.bodySmall,
                    color = activeTheme.secondaryText
                )
            }

            // Palette Swatch Chips
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(spec.backgroundTop)
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(spec.digitKeyBg)
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(spec.operatorKeyBg)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(spec.accentColor)
                )
            }
        }
    }
}
