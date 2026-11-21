package com.example.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp

enum class NumiFontStyle {
    OUTFIT,
    FREDOKA,
    JETBRAINS_MONO,
    SPACE_GROTESK
}

data class NumiThemeSpec(
    val id: ThemeId,
    val name: String,
    val tagline: String,
    val isDark: Boolean,
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val displaySurface: Color,
    val displayBorder: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val extrusionColor: Color,
    val shadowColor: Color,
    val highlightColor: Color,
    val digitKeyBg: Color,
    val digitKeyText: Color,
    val digitKeyBevel: Color,
    val operatorKeyBg: Color,
    val operatorKeyText: Color,
    val operatorKeyBevel: Color,
    val utilityKeyBg: Color,
    val utilityKeyText: Color,
    val utilityKeyBevel: Color,
    val equalsKeyBg: Color,
    val equalsKeyText: Color,
    val equalsKeyBevel: Color,
    val scientificKeyBg: Color,
    val scientificKeyText: Color,
    val scientificKeyBevel: Color,
    val accentColor: Color,
    val secondaryAccentColor: Color,
    val neonGlowColor: Color?,
    val fontStyle: NumiFontStyle,
    val keyCornerRadius: Dp,
    val displayCornerRadius: Dp,
    val keyElevationDepth: Dp
)
