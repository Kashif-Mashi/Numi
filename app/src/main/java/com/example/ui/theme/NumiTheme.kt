package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.animations.NumiMotion
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.NumiThemeSpec
import com.example.domain.models.TextDepthStyle
import com.example.domain.models.TextShadowStyle
import com.example.ui.colors.NumiPalettes
import com.example.ui.typography.buildNumiTypography

data class NumiTextCustomization(
    val depthStyle: TextDepthStyle = TextDepthStyle.MEDIUM,
    val shadowStyle: TextShadowStyle = TextShadowStyle.SOFT,
    val numberScaleFactor: Float = 1.0f
)

val LocalNumiTheme = staticCompositionLocalOf { NumiPalettes.CreamClassic }
val LocalAnimationIntensity = staticCompositionLocalOf { AnimationIntensity.PLAYFUL }
val LocalNumiTextCustomization = staticCompositionLocalOf { NumiTextCustomization() }

@Composable
fun NumiTheme(
    themeSpec: NumiThemeSpec,
    animationIntensity: AnimationIntensity = AnimationIntensity.PLAYFUL,
    textCustomization: NumiTextCustomization = NumiTextCustomization(),
    content: @Composable () -> Unit
) {
    val colorSpec = NumiMotion.themeColorSpec()
    val bgTop by animateColorAsState(themeSpec.backgroundTop, colorSpec, label = "bgTop")
    val bgBottom by animateColorAsState(themeSpec.backgroundBottom, colorSpec, label = "bgBottom")
    val displaySurface by animateColorAsState(themeSpec.displaySurface, colorSpec, label = "displaySurface")
    val displayBorder by animateColorAsState(themeSpec.displayBorder, colorSpec, label = "displayBorder")
    val primaryText by animateColorAsState(themeSpec.primaryText, colorSpec, label = "primaryText")
    val secondaryText by animateColorAsState(themeSpec.secondaryText, colorSpec, label = "secondaryText")
    val extrusionColor by animateColorAsState(themeSpec.extrusionColor, colorSpec, label = "extrusion")
    val digitBg by animateColorAsState(themeSpec.digitKeyBg, colorSpec, label = "digitBg")
    val digitText by animateColorAsState(themeSpec.digitKeyText, colorSpec, label = "digitText")
    val digitBevel by animateColorAsState(themeSpec.digitKeyBevel, colorSpec, label = "digitBevel")
    val opBg by animateColorAsState(themeSpec.operatorKeyBg, colorSpec, label = "opBg")
    val opText by animateColorAsState(themeSpec.operatorKeyText, colorSpec, label = "opText")
    val opBevel by animateColorAsState(themeSpec.operatorKeyBevel, colorSpec, label = "opBevel")
    val utilBg by animateColorAsState(themeSpec.utilityKeyBg, colorSpec, label = "utilBg")
    val utilText by animateColorAsState(themeSpec.utilityKeyText, colorSpec, label = "utilText")
    val utilBevel by animateColorAsState(themeSpec.utilityKeyBevel, colorSpec, label = "utilBevel")
    val eqBg by animateColorAsState(themeSpec.equalsKeyBg, colorSpec, label = "eqBg")
    val eqText by animateColorAsState(themeSpec.equalsKeyText, colorSpec, label = "eqText")
    val eqBevel by animateColorAsState(themeSpec.equalsKeyBevel, colorSpec, label = "eqBevel")
    val sciBg by animateColorAsState(themeSpec.scientificKeyBg, colorSpec, label = "sciBg")
    val sciText by animateColorAsState(themeSpec.scientificKeyText, colorSpec, label = "sciText")
    val sciBevel by animateColorAsState(themeSpec.scientificKeyBevel, colorSpec, label = "sciBevel")
    val accent by animateColorAsState(themeSpec.accentColor, colorSpec, label = "accent")
    val secondaryAccent by animateColorAsState(themeSpec.secondaryAccentColor, colorSpec, label = "secAccent")

    val animatedSpec = themeSpec.copy(
        backgroundTop = bgTop,
        backgroundBottom = bgBottom,
        displaySurface = displaySurface,
        displayBorder = displayBorder,
        primaryText = primaryText,
        secondaryText = secondaryText,
        extrusionColor = extrusionColor,
        digitKeyBg = digitBg,
        digitKeyText = digitText,
        digitKeyBevel = digitBevel,
        operatorKeyBg = opBg,
        operatorKeyText = opText,
        operatorKeyBevel = opBevel,
        utilityKeyBg = utilBg,
        utilityKeyText = utilText,
        utilityKeyBevel = utilBevel,
        equalsKeyBg = eqBg,
        equalsKeyText = eqText,
        equalsKeyBevel = eqBevel,
        scientificKeyBg = sciBg,
        scientificKeyText = sciText,
        scientificKeyBevel = sciBevel,
        accentColor = accent,
        secondaryAccentColor = secondaryAccent
    )

    val m3Scheme = if (animatedSpec.isDark) {
        darkColorScheme(
            primary = animatedSpec.accentColor,
            onPrimary = animatedSpec.operatorKeyText,
            secondary = animatedSpec.secondaryAccentColor,
            background = animatedSpec.backgroundTop,
            onBackground = animatedSpec.primaryText,
            surface = animatedSpec.displaySurface,
            onSurface = animatedSpec.primaryText,
            surfaceVariant = animatedSpec.digitKeyBg,
            onSurfaceVariant = animatedSpec.secondaryText,
            outline = animatedSpec.displayBorder
        )
    } else {
        lightColorScheme(
            primary = animatedSpec.accentColor,
            onPrimary = animatedSpec.operatorKeyText,
            secondary = animatedSpec.secondaryAccentColor,
            background = animatedSpec.backgroundTop,
            onBackground = animatedSpec.primaryText,
            surface = animatedSpec.displaySurface,
            onSurface = animatedSpec.primaryText,
            surfaceVariant = animatedSpec.digitKeyBg,
            onSurfaceVariant = animatedSpec.secondaryText,
            outline = animatedSpec.displayBorder
        )
    }

    CompositionLocalProvider(
        LocalNumiTheme provides animatedSpec,
        LocalAnimationIntensity provides animationIntensity,
        LocalNumiTextCustomization provides textCustomization
    ) {
        MaterialTheme(
            colorScheme = m3Scheme,
            typography = buildNumiTypography(animatedSpec.fontStyle),
            content = content
        )
    }
}
