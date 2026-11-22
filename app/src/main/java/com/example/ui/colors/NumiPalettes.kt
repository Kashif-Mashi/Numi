package com.example.ui.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.example.domain.models.NumiFontStyle
import com.example.domain.models.NumiThemeSpec
import com.example.domain.models.ThemeId
import com.example.domain.models.UserPreferences

object NumiPalettes {

    val Midnight = NumiThemeSpec(
        id = ThemeId.MIDNIGHT,
        name = "Midnight Dark",
        tagline = "Obsidian matte & warm amber",
        isDark = true,
        backgroundTop = Color(0xFF090A0F),
        backgroundBottom = Color(0xFF13151F),
        displaySurface = Color(0xFF171923),
        displayBorder = Color(0xFF272B3A),
        primaryText = Color(0xFFF8FAFC),
        secondaryText = Color(0xFF94A3B8),
        extrusionColor = Color(0xFF343A4F),
        shadowColor = Color(0x70000000),
        highlightColor = Color(0x28FFFFFF),
        digitKeyBg = Color(0xFF1F2330),
        digitKeyText = Color(0xFFF8FAFC),
        digitKeyBevel = Color(0xFF0E1017),
        operatorKeyBg = Color(0xFFFF9F1C),
        operatorKeyText = Color(0xFF14110B),
        operatorKeyBevel = Color(0xFFC97300),
        utilityKeyBg = Color(0xFF2D3244),
        utilityKeyText = Color(0xFFE2E8F0),
        utilityKeyBevel = Color(0xFF161923),
        equalsKeyBg = Color(0xFFF8FAFC),
        equalsKeyText = Color(0xFF0B0D14),
        equalsKeyBevel = Color(0xFFCBD5E1),
        scientificKeyBg = Color(0xFF171A25),
        scientificKeyText = Color(0xFFCBD5E1),
        scientificKeyBevel = Color(0xFF0B0D13),
        accentColor = Color(0xFFFF9F1C),
        secondaryAccentColor = Color(0xFFFF4D6D),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val CleanLight = NumiThemeSpec(
        id = ThemeId.CYBER_NEON,
        name = "Clean Light",
        tagline = "Crisp minimalist white & slate",
        isDark = false,
        backgroundTop = Color(0xFFF8FAFC),
        backgroundBottom = Color(0xFFEDF2F7),
        displaySurface = Color(0xFFFFFFFF),
        displayBorder = Color(0xFFE2E8F0),
        primaryText = Color(0xFF0F172A),
        secondaryText = Color(0xFF64748B),
        extrusionColor = Color(0xFFCBD5E1),
        shadowColor = Color(0x200F172A),
        highlightColor = Color(0xFFFFFFFF),
        digitKeyBg = Color(0xFFFFFFFF),
        digitKeyText = Color(0xFF0F172A),
        digitKeyBevel = Color(0xFFE2E8F0),
        operatorKeyBg = Color(0xFF0284C7),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFF0369A1),
        utilityKeyBg = Color(0xFFE2E8F0),
        utilityKeyText = Color(0xFF1E293B),
        utilityKeyBevel = Color(0xFFCBD5E1),
        equalsKeyBg = Color(0xFF0F172A),
        equalsKeyText = Color(0xFFFFFFFF),
        equalsKeyBevel = Color(0xFF000000),
        scientificKeyBg = Color(0xFFF1F5F9),
        scientificKeyText = Color(0xFF334155),
        scientificKeyBevel = Color(0xFFE2E8F0),
        accentColor = Color(0xFF0284C7),
        secondaryAccentColor = Color(0xFF0EA5E9),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val SlateDark = NumiThemeSpec(
        id = ThemeId.CITRUS_POP,
        name = "Slate Charcoal",
        tagline = "Deep charcoal & vibrant azure",
        isDark = true,
        backgroundTop = Color(0xFF111827),
        backgroundBottom = Color(0xFF1F2937),
        displaySurface = Color(0xFF1F2937),
        displayBorder = Color(0xFF374151),
        primaryText = Color(0xFFF9FAFB),
        secondaryText = Color(0xFF9CA3AF),
        extrusionColor = Color(0xFF4B5563),
        shadowColor = Color(0x70000000),
        highlightColor = Color(0x28FFFFFF),
        digitKeyBg = Color(0xFF374151),
        digitKeyText = Color(0xFFF9FAFB),
        digitKeyBevel = Color(0xFF1F2937),
        operatorKeyBg = Color(0xFF3B82F6),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFF1D4ED8),
        utilityKeyBg = Color(0xFF4B5563),
        utilityKeyText = Color(0xFFF3F4F6),
        utilityKeyBevel = Color(0xFF374151),
        equalsKeyBg = Color(0xFF60A5FA),
        equalsKeyText = Color(0xFF0F172A),
        equalsKeyBevel = Color(0xFF2563EB),
        scientificKeyBg = Color(0xFF1F2937),
        scientificKeyText = Color(0xFFE5E7EB),
        scientificKeyBevel = Color(0xFF111827),
        accentColor = Color(0xFF3B82F6),
        secondaryAccentColor = Color(0xFF60A5FA),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val OceanBlue = NumiThemeSpec(
        id = ThemeId.OCEAN_BLUE,
        name = "Ocean Blue",
        tagline = "Deep navy & sky blue",
        isDark = true,
        backgroundTop = Color(0xFF0A192F),
        backgroundBottom = Color(0xFF112A46),
        displaySurface = Color(0xFF132D4E),
        displayBorder = Color(0xFF234873),
        primaryText = Color(0xFFE6F4FF),
        secondaryText = Color(0xFF88B4D8),
        extrusionColor = Color(0xFF245580),
        shadowColor = Color(0x66020C1B),
        highlightColor = Color(0x287DD3FC),
        digitKeyBg = Color(0xFF193558),
        digitKeyText = Color(0xFFE6F4FF),
        digitKeyBevel = Color(0xFF0B1C32),
        operatorKeyBg = Color(0xFF0EA5E9),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFF0369A1),
        utilityKeyBg = Color(0xFF23476F),
        utilityKeyText = Color(0xFFBAE6FD),
        utilityKeyBevel = Color(0xFF122842),
        equalsKeyBg = Color(0xFF38BDF8),
        equalsKeyText = Color(0xFF082F49),
        equalsKeyBevel = Color(0xFF0284C7),
        scientificKeyBg = Color(0xFF132B47),
        scientificKeyText = Color(0xFF7DD3FC),
        scientificKeyBevel = Color(0xFF091627),
        accentColor = Color(0xFF38BDF8),
        secondaryAccentColor = Color(0xFF0284C7),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val ForestGreen = NumiThemeSpec(
        id = ThemeId.PASTEL_DREAM,
        name = "Forest Emerald",
        tagline = "Deep moss & emerald sage",
        isDark = true,
        backgroundTop = Color(0xFF061A14),
        backgroundBottom = Color(0xFF0D281E),
        displaySurface = Color(0xFF113227),
        displayBorder = Color(0xFF1E4D3D),
        primaryText = Color(0xFFECFDF5),
        secondaryText = Color(0xFF6EE7B7),
        extrusionColor = Color(0xFF064E3B),
        shadowColor = Color(0x70000000),
        highlightColor = Color(0x28FFFFFF),
        digitKeyBg = Color(0xFF173F32),
        digitKeyText = Color(0xFFECFDF5),
        digitKeyBevel = Color(0xFF0B241C),
        operatorKeyBg = Color(0xFF10B981),
        operatorKeyText = Color(0xFF06281D),
        operatorKeyBevel = Color(0xFF059669),
        utilityKeyBg = Color(0xFF1F4E3E),
        utilityKeyText = Color(0xFFA7F3D0),
        utilityKeyBevel = Color(0xFF11352A),
        equalsKeyBg = Color(0xFF34D399),
        equalsKeyText = Color(0xFF022C22),
        equalsKeyBevel = Color(0xFF059669),
        scientificKeyBg = Color(0xFF0E2E23),
        scientificKeyText = Color(0xFF6EE7B7),
        scientificKeyBevel = Color(0xFF071B14),
        accentColor = Color(0xFF10B981),
        secondaryAccentColor = Color(0xFF34D399),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val CreamClassic = NumiThemeSpec(
        id = ThemeId.CREAM_CLASSIC,
        name = "Classic Cream",
        tagline = "Warm paper & terracotta",
        isDark = false,
        backgroundTop = Color(0xFFF7F1E5),
        backgroundBottom = Color(0xFFEDE3D0),
        displaySurface = Color(0xFFFFFCF6),
        displayBorder = Color(0xFFE0D5C1),
        primaryText = Color(0xFF1A1816),
        secondaryText = Color(0xFF6B6257),
        extrusionColor = Color(0xFFD6C8B0),
        shadowColor = Color(0x331A1816),
        highlightColor = Color(0xFFFFFFFF),
        digitKeyBg = Color(0xFFFFFDF9),
        digitKeyText = Color(0xFF1A1816),
        digitKeyBevel = Color(0xFFD9CEBC),
        operatorKeyBg = Color(0xFFEE6338),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFFC1431D),
        utilityKeyBg = Color(0xFFE4DAC7),
        utilityKeyText = Color(0xFF2D2721),
        utilityKeyBevel = Color(0xFFC7BAA3),
        equalsKeyBg = Color(0xFF1D1B19),
        equalsKeyText = Color(0xFFFFF8EC),
        equalsKeyBevel = Color(0xFF000000),
        scientificKeyBg = Color(0xFFF3EBDD),
        scientificKeyText = Color(0xFF3E362E),
        scientificKeyBevel = Color(0xFFD7CAB3),
        accentColor = Color(0xFFEE6338),
        secondaryAccentColor = Color(0xFF2F6FED),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.OUTFIT,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val presetThemes: List<NumiThemeSpec> = listOf(
        Midnight,
        CleanLight,
        SlateDark,
        OceanBlue,
        ForestGreen,
        CreamClassic
    )

    fun buildCustomTheme(accentHex: Long, isDark: Boolean): NumiThemeSpec {
        val accent = Color(accentHex.toInt())
        val onAccent = if (accent.luminance() > 0.45f) Color(0xFF111827) else Color(0xFFFFFFFF)
        val bevelAccent = lerp(accent, Color.Black, 0.28f)

        return if (isDark) {
            NumiThemeSpec(
                id = ThemeId.CUSTOM,
                name = "Custom Color",
                tagline = "Your chosen dark color palette",
                isDark = true,
                backgroundTop = lerp(Color(0xFF0B0E14), accent, 0.08f),
                backgroundBottom = lerp(Color(0xFF141923), accent, 0.14f),
                displaySurface = lerp(Color(0xFF191F2C), accent, 0.07f),
                displayBorder = lerp(Color(0xFF2C3549), accent, 0.25f),
                primaryText = Color(0xFFF8FAFC),
                secondaryText = lerp(Color(0xFF94A3B8), accent, 0.25f),
                extrusionColor = lerp(Color(0xFF334155), accent, 0.35f),
                shadowColor = Color(0x75000000),
                highlightColor = lerp(Color.White, accent, 0.25f).copy(alpha = 0.2f),
                digitKeyBg = Color(0xFF202737),
                digitKeyText = Color(0xFFF8FAFC),
                digitKeyBevel = Color(0xFF0E121B),
                operatorKeyBg = accent,
                operatorKeyText = onAccent,
                operatorKeyBevel = bevelAccent,
                utilityKeyBg = Color(0xFF2D374B),
                utilityKeyText = Color(0xFFE2E8F0),
                utilityKeyBevel = Color(0xFF151A25),
                equalsKeyBg = lerp(accent, Color.White, 0.22f),
                equalsKeyText = Color(0xFF0B0F17),
                equalsKeyBevel = bevelAccent,
                scientificKeyBg = Color(0xFF181E2B),
                scientificKeyText = lerp(Color.White, accent, 0.4f),
                scientificKeyBevel = Color(0xFF0B0E15),
                accentColor = accent,
                secondaryAccentColor = lerp(accent, Color.White, 0.35f),
                neonGlowColor = null,
                fontStyle = NumiFontStyle.OUTFIT,
                keyCornerRadius = 20.dp,
                displayCornerRadius = 28.dp,
                keyElevationDepth = 5.dp
            )
        } else {
            NumiThemeSpec(
                id = ThemeId.CUSTOM,
                name = "Custom Color",
                tagline = "Your chosen light color palette",
                isDark = false,
                backgroundTop = lerp(Color(0xFFF8F9FC), accent, 0.06f),
                backgroundBottom = lerp(Color(0xFFEDF0F7), accent, 0.12f),
                displaySurface = Color(0xFFFFFFFF),
                displayBorder = lerp(Color(0xFFDCE1EC), accent, 0.25f),
                primaryText = Color(0xFF181A20),
                secondaryText = Color(0xFF5D6475),
                extrusionColor = lerp(Color(0xFFD0D6E2), accent, 0.28f),
                shadowColor = Color(0x2A181A20),
                highlightColor = Color.White,
                digitKeyBg = Color(0xFFFFFFFF),
                digitKeyText = Color(0xFF181A20),
                digitKeyBevel = Color(0xFFD6DCE8),
                operatorKeyBg = accent,
                operatorKeyText = onAccent,
                operatorKeyBevel = bevelAccent,
                utilityKeyBg = lerp(Color(0xFFE4E8F2), accent, 0.12f),
                utilityKeyText = Color(0xFF1F2430),
                utilityKeyBevel = Color(0xFFC3CAD9),
                equalsKeyBg = Color(0xFF181A20),
                equalsKeyText = Color(0xFFFFFFFF),
                equalsKeyBevel = Color(0xFF000000),
                scientificKeyBg = lerp(Color(0xFFF0F3FA), accent, 0.08f),
                scientificKeyText = Color(0xFF333B4D),
                scientificKeyBevel = Color(0xFFD2D8E6),
                accentColor = accent,
                secondaryAccentColor = lerp(accent, Color.Black, 0.2f),
                neonGlowColor = null,
                fontStyle = NumiFontStyle.OUTFIT,
                keyCornerRadius = 20.dp,
                displayCornerRadius = 28.dp,
                keyElevationDepth = 5.dp
            )
        }
    }

    fun resolveTheme(preferences: UserPreferences): NumiThemeSpec {
        val base = when (preferences.themeId) {
            ThemeId.MIDNIGHT -> Midnight
            ThemeId.CYBER_NEON -> CleanLight
            ThemeId.CITRUS_POP -> SlateDark
            ThemeId.OCEAN_BLUE -> OceanBlue
            ThemeId.PASTEL_DREAM -> ForestGreen
            ThemeId.CREAM_CLASSIC -> CreamClassic
            ThemeId.CUSTOM -> buildCustomTheme(preferences.customAccentHex, preferences.customIsDark)
        }
        return base
    }
}
