package com.example.presentation.themes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.ui.typography.NumiFonts

private enum class StudioTab(val label: String) {
    PALETTES("Palettes"),
    TEXT_STYLE("Text & 3D")
}

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
    var activeTab by remember { mutableStateOf(StudioTab.PALETTES) }
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current

    val customSwatches = listOf(
        0xFF6366F1L,
        0xFFEC4899L,
        0xFF10B981L,
        0xFFF59E0BL,
        0xFFEF4444L,
        0xFF06B6D4L
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(activeTheme.backgroundTop, activeTheme.backgroundBottom))
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(activeTheme.displaySurface)
                        .border(1.dp, activeTheme.displayBorder, RoundedCornerShape(16.dp))
                        .clickable(role = Role.Button, onClick = onNavigateBack)
                        .testTag("themes_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.cd_navigate_back),
                        tint = activeTheme.primaryText
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.themes_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = activeTheme.primaryText
                    )
                    Text(
                        text = stringResource(R.string.themes_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = activeTheme.secondaryText
                    )
                }
            }
        }

        // Pinned Live Interactive 3D Preview Stage using the previewed theme & text customization!
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            NumiTheme(
                themeSpec = preview,
                textCustomization = NumiTextCustomization(
                    depthStyle = uiState.textDepthStyle,
                    shadowStyle = uiState.textShadowStyle,
                    numberScaleFactor = uiState.numberScaleFactor
                )
            ) {
                val pTheme = LocalNumiTheme.current
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(pTheme.backgroundTop, pTheme.backgroundBottom)
                            )
                        )
                        .border(1.5.dp, pTheme.displayBorder, RoundedCornerShape(26.dp))
                        .padding(12.dp)
                ) {
                    Interactive3DNumberDisplay(
                        expression = "24 × 1.78",
                        primaryDisplay = "42.72",
                        livePreview = null,
                        isError = false,
                        justEvaluated = true,
                        pulseTrigger = 0,
                        angleUnit = AngleUnit.DEG,
                        isScientificMode = false,
                        onToggleAngleUnit = {},
                        modifier = Modifier.height(138.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sample tactile mini keycaps + Apply CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MiniKeyPreview("7", pTheme.digitKeyBg, pTheme.digitKeyText, pTheme.digitKeyBevel, pTheme)
                        MiniKeyPreview("+", pTheme.operatorKeyBg, pTheme.operatorKeyText, pTheme.operatorKeyBevel, pTheme)
                        MiniKeyPreview("=", pTheme.equalsKeyBg, pTheme.equalsKeyText, pTheme.equalsKeyBevel, pTheme)

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .defaultMinSize(minWidth = 112.dp, minHeight = 48.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(pTheme.equalsKeyBg)
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
                                .padding(horizontal = 18.dp, vertical = 12.dp)
                                .testTag("themes_apply_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isPreviewActive) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = pTheme.equalsKeyText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = if (isPreviewActive) stringResource(R.string.themes_active)
                                    else stringResource(R.string.themes_apply),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = pTheme.equalsKeyText
                                )
                            }
                        }
                    }
                }
            }
        }

        // Studio Tab Switcher: Palettes vs Text & 3D Style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StudioTab.entries.forEach { tab ->
                val selected = activeTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selected) activeTheme.accentColor else activeTheme.displaySurface)
                        .border(
                            1.dp,
                            if (selected) activeTheme.accentColor else activeTheme.displayBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable(role = Role.Button) { activeTab = tab }
                        .testTag("studio_tab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected) activeTheme.operatorKeyText else activeTheme.primaryText
                    )
                }
            }
        }

        if (activeTab == StudioTab.PALETTES) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("themes_list"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Custom Studio Controls when Custom is previewed
                item(key = "custom_controls") {
                    AnimatedVisibility(visible = uiState.previewThemeId == ThemeId.CUSTOM) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(activeTheme.displaySurface)
                                .border(1.dp, activeTheme.displayBorder, RoundedCornerShape(22.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Customize Accent & Base",
                                style = MaterialTheme.typography.titleMedium,
                                color = activeTheme.primaryText
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                customSwatches.forEach { hex ->
                                    val selected = uiState.customAccentHex == hex
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color(hex.toInt()))
                                            .border(
                                                width = if (selected) 3.dp else 1.dp,
                                                color = if (selected) activeTheme.primaryText else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable { onPreviewCustomAccent(hex) }
                                            .testTag("custom_swatch_${hex.toString(16)}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = "Selected accent",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                BaseToneChip(
                                    label = "Light Base",
                                    icon = Icons.Rounded.LightMode,
                                    selected = !uiState.customIsDark,
                                    theme = activeTheme,
                                    onClick = { onPreviewCustomDark(false) }
                                )
                                BaseToneChip(
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

                // Theme Gallery List
                items(uiState.allThemes, key = { it.id.name }) { spec ->
                    ThemeCard(
                        spec = spec,
                        isActive = spec.id == uiState.activeThemeId,
                        isPreviewed = spec.id == uiState.previewThemeId,
                        activeTheme = activeTheme,
                        onClick = { onPreviewTheme(spec.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        } else {
            // Visual Text & 3D Customization Panel
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("text_customization_panel"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    CustomizationCard(title = "Number Typography", theme = activeTheme) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextFontOverride.entries.forEach { fontOption ->
                                val selected = uiState.textFontOverride == fontOption
                                OptionChip(
                                    label = fontOption.label,
                                    selected = selected,
                                    theme = activeTheme,
                                    testTag = "font_option_${fontOption.name.lowercase()}",
                                    onClick = { onSetTextFontOverride(fontOption) }
                                )
                            }
                        }
                    }
                }

                item {
                    CustomizationCard(title = "3D Text Extrusion Depth", theme = activeTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextDepthStyle.entries.forEach { depthOption ->
                                val selected = uiState.textDepthStyle == depthOption
                                OptionChip(
                                    label = depthOption.label,
                                    selected = selected,
                                    theme = activeTheme,
                                    testTag = "depth_option_${depthOption.name.lowercase()}",
                                    modifier = Modifier.weight(1f),
                                    onClick = { onSetTextDepthStyle(depthOption) }
                                )
                            }
                        }
                    }
                }

                item {
                    CustomizationCard(title = "Text Shadow & Glow", theme = activeTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextShadowStyle.entries.forEach { shadowOption ->
                                val selected = uiState.textShadowStyle == shadowOption
                                OptionChip(
                                    label = shadowOption.label,
                                    selected = selected,
                                    theme = activeTheme,
                                    testTag = "shadow_option_${shadowOption.name.lowercase()}",
                                    modifier = Modifier.weight(1f),
                                    onClick = { onSetTextShadowStyle(shadowOption) }
                                )
                            }
                        }
                    }
                }

                item {
                    val scalePresets = listOf(0.88f to "Compact", 1.0f to "Regular", 1.12f to "Bold XL")
                    CustomizationCard(title = "Display Number Size", theme = activeTheme) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            scalePresets.forEach { (scaleVal, label) ->
                                val selected = kotlin.math.abs(uiState.numberScaleFactor - scaleVal) < 0.03f
                                OptionChip(
                                    label = label,
                                    selected = selected,
                                    theme = activeTheme,
                                    testTag = "scale_option_${label.lowercase().replace(" ", "_")}",
                                    modifier = Modifier.weight(1f),
                                    onClick = { onSetNumberScaleFactor(scaleVal) }
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun CustomizationCard(
    title: String,
    theme: NumiThemeSpec,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(theme.displaySurface)
            .border(1.dp, theme.displayBorder, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.TextFields,
                contentDescription = null,
                tint = theme.accentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = theme.primaryText
            )
        }
        content()
    }
}

@Composable
private fun OptionChip(
    label: String,
    selected: Boolean,
    theme: NumiThemeSpec,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) theme.accentColor else theme.utilityKeyBg)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) theme.operatorKeyText else theme.utilityKeyText,
            maxLines = 1
        )
    }
}

@Composable
private fun MiniKeyPreview(
    label: String,
    bg: Color,
    fg: Color,
    bevel: Color,
    theme: NumiThemeSpec
) {
    val shape = RoundedCornerShape(14.dp)
    Box(modifier = Modifier.size(44.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 3.dp)
                .clip(shape)
                .background(bevel)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontWeight = FontWeight.Bold
                ),
                color = fg
            )
        }
    }
}

@Composable
private fun BaseToneChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    theme: NumiThemeSpec,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) theme.accentColor else theme.utilityKeyBg)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) theme.operatorKeyText else theme.utilityKeyText,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) theme.operatorKeyText else theme.utilityKeyText
        )
    }
}

@Composable
private fun ThemeCard(
    spec: NumiThemeSpec,
    isActive: Boolean,
    isPreviewed: Boolean,
    activeTheme: NumiThemeSpec,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isPreviewed) 8.dp else 2.dp, shape)
            .clip(shape)
            .background(activeTheme.displaySurface)
            .border(
                width = if (isPreviewed) 2.dp else 1.dp,
                color = if (isPreviewed) activeTheme.accentColor else activeTheme.displayBorder,
                shape = shape
            )
            .clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp)
            .testTag("theme_card_${spec.id.storageKey}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(spec.backgroundTop, spec.backgroundBottom)))
                    .border(1.dp, spec.displayBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(spec.operatorKeyBg)
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(spec.equalsKeyBg)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = spec.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = NumiFonts.displayFamily(spec.fontStyle),
                            fontSize = 18.sp
                        ),
                        color = activeTheme.primaryText
                    )
                    if (isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(activeTheme.accentColor.copy(alpha = 0.16f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = activeTheme.accentColor
                            )
                        }
                    }
                }
                Text(
                    text = spec.tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = activeTheme.secondaryText
                )
            }
        }

        if (isPreviewed) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(activeTheme.accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Previewing",
                    tint = activeTheme.operatorKeyText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
