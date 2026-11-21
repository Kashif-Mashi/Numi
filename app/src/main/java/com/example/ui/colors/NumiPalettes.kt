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

    val CreamClassic = NumiThemeSpec(
        id = ThemeId.CREAM_CLASSIC,
        name = "Cream Classic",
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
        keyCornerRadius = 22.dp,
        displayCornerRadius = 30.dp,
        keyElevationDepth = 5.dp
    )

    val Midnight = NumiThemeSpec(
        id = ThemeId.MIDNIGHT,
        name = "Midnight",
        tagline = "Obsidian matte & amber",
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
        fontStyle = NumiFontStyle.SPACE_GROTESK,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val CitrusPop = NumiThemeSpec(
        id = ThemeId.CITRUS_POP,
        name = "Citrus Pop",
        tagline = "Sunlit tangerine & lemon",
        isDark = false,
        backgroundTop = Color(0xFFFFF4D9),
        backgroundBottom = Color(0xFFFFE1A8),
        displaySurface = Color(0xFFFFFBF0),
        displayBorder = Color(0xFFF6C667),
        primaryText = Color(0xFF2A180B),
        secondaryText = Color(0xFF8C5320),
        extrusionColor = Color(0xFFF59E0B),
        shadowColor = Color(0x38D97706),
        highlightColor = Color(0xFFFFFFFF),
        digitKeyBg = Color(0xFFFFFDF7),
        digitKeyText = Color(0xFF2A180B),
        digitKeyBevel = Color(0xFFF2C86B),
        operatorKeyBg = Color(0xFFFF6B00),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFFC94B00),
        utilityKeyBg = Color(0xFFFFD426),
        utilityKeyText = Color(0xFF2A180B),
        utilityKeyBevel = Color(0xFFD9A900),
        equalsKeyBg = Color(0xFF10B981),
        equalsKeyText = Color(0xFFFFFFFF),
        equalsKeyBevel = Color(0xFF05875C),
        scientificKeyBg = Color(0xFFFFEEC2),
        scientificKeyText = Color(0xFF5C3410),
        scientificKeyBevel = Color(0xFFE6BE67),
        accentColor = Color(0xFFFF6B00),
        secondaryAccentColor = Color(0xFF10B981),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.FREDOKA,
        keyCornerRadius = 26.dp,
        displayCornerRadius = 32.dp,
        keyElevationDepth = 6.dp
    )

    val CyberNeon = NumiThemeSpec(
        id = ThemeId.CYBER_NEON,
        name = "Cyber Neon",
        tagline = "Electric cyan & matrix glow",
        isDark = true,
        backgroundTop = Color(0xFF070A12),
        backgroundBottom = Color(0xFF0E1426),
        displaySurface = Color(0xFF0F172A),
        displayBorder = Color(0xFF1E3A5F),
        primaryText = Color(0xFFECFEFF),
        secondaryText = Color(0xFF67E8F9),
        extrusionColor = Color(0xFF0891B2),
        shadowColor = Color(0x88000000),
        highlightColor = Color(0x3322D3EE),
        digitKeyBg = Color(0xFF131D35),
        digitKeyText = Color(0xFFECFEFF),
        digitKeyBevel = Color(0xFF080E1C),
        operatorKeyBg = Color(0xFFA855F7),
        operatorKeyText = Color(0xFFFFFFFF),
        operatorKeyBevel = Color(0xFF6B21A8),
        utilityKeyBg = Color(0xFF162544),
        utilityKeyText = Color(0xFF22D3EE),
        utilityKeyBevel = Color(0xFF0A1326),
        equalsKeyBg = Color(0xFF10F59D),
        equalsKeyText = Color(0xFF042217),
        equalsKeyBevel = Color(0xFF059669),
        scientificKeyBg = Color(0xFF10192E),
        scientificKeyText = Color(0xFFC084FC),
        scientificKeyBevel = Color(0xFF070C18),
        accentColor = Color(0xFF22D3EE),
        secondaryAccentColor = Color(0xFFA855F7),
        neonGlowColor = Color(0xFF10F59D),
        fontStyle = NumiFontStyle.JETBRAINS_MONO,
        keyCornerRadius = 16.dp,
        displayCornerRadius = 24.dp,
        keyElevationDepth = 4.dp
    )

    val PastelDream = NumiThemeSpec(
        id = ThemeId.PASTEL_DREAM,
        name = "Pastel Dream",
        tagline = "Cloud lavender & mint",
        isDark = false,
        backgroundTop = Color(0xFFFAF1FC),
        backgroundBottom = Color(0xFFEDF6FF),
        displaySurface = Color(0xFFFFFFFF),
        displayBorder = Color(0xFFE7DAF3),
        primaryText = Color(0xFF2D2440),
        secondaryText = Color(0xFF7B6F96),
        extrusionColor = Color(0xFFD2C1F0),
        shadowColor = Color(0x285B468A),
        highlightColor = Color(0xFFFFFFFF),
        digitKeyBg = Color(0xFFFFFFFF),
        digitKeyText = Color(0xFF2D2440),
        digitKeyBevel = Color(0xFFDFD5ED),
        operatorKeyBg = Color(0xFFFF9EBB),
        operatorKeyText = Color(0xFF3E1929),
        operatorKeyBevel = Color(0xFFE07798),
        utilityKeyBg = Color(0xFFBDF2DF),
        utilityKeyText = Color(0xFF164336),
        utilityKeyBevel = Color(0xFF8FD6BE),
        equalsKeyBg = Color(0xFF907AD6),
        equalsKeyText = Color(0xFFFFFFFF),
        equalsKeyBevel = Color(0xFF6B53B8),
        scientificKeyBg = Color(0xFFEDE6FA),
        scientificKeyText = Color(0xFF4C3D70),
        scientificKeyBevel = Color(0xFFD2C4EB),
        accentColor = Color(0xFF907AD6),
        secondaryAccentColor = Color(0xFFFF9EBB),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.FREDOKA,
        keyCornerRadius = 28.dp,
        displayCornerRadius = 32.dp,
        keyElevationDepth = 5.dp
    )

    val OceanBlue = NumiThemeSpec(
        id = ThemeId.OCEAN_BLUE,
        name = "Ocean Blue",
        tagline = "Deep abyss & seafoam",
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
        equalsKeyBg = Color(0xFF2DD4BF),
        equalsKeyText = Color(0xFF062C2B),
        equalsKeyBevel = Color(0xFF0D9488),
        scientificKeyBg = Color(0xFF132B47),
        scientificKeyText = Color(0xFF7DD3FC),
        scientificKeyBevel = Color(0xFF091627),
        accentColor = Color(0xFF38BDF8),
        secondaryAccentColor = Color(0xFF2DD4BF),
        neonGlowColor = null,
        fontStyle = NumiFontStyle.SPACE_GROTESK,
        keyCornerRadius = 20.dp,
        displayCornerRadius = 28.dp,
        keyElevationDepth = 5.dp
    )

    val presetThemes: List<NumiThemeSpec> = listOf(
        CreamClassic,
        Midnight,
        CitrusPop,
        CyberNeon,
        PastelDream,
        OceanBlue
    )

    fun buildCustomTheme(accentHex: Long, isDark: Boolean): NumiThemeSpec {
        val accent = Color(accentHex.toInt())
        val onAccent = if (accent.luminance() > 0.45f) Color(0xFF111827) else Color(0xFFFFFFFF)
        val bevelAccent = lerp(accent, Color.Black, 0.28f)

        return if (isDark) {
            NumiThemeSpec(
                id = ThemeId.CUSTOM,
                name = "Custom Studio",
                tagline = "Your signature dark palette",
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
                keyCornerRadius = 22.dp,
                displayCornerRadius = 28.dp,
                keyElevationDepth = 5.dp
            )
        } else {
            NumiThemeSpec(
                id = ThemeId.CUSTOM,
                name = "Custom Studio",
                tagline = "Your signature light palette",
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
                keyCornerRadius = 22.dp,
                displayCornerRadius = 28.dp,
                keyElevationDepth = 5.dp
            )
        }
    }

    fun resolveTheme(preferences: UserPreferences): NumiThemeSpec {
        val base = when (preferences.themeId) {
            ThemeId.CREAM_CLASSIC -> CreamClassic
            ThemeId.MIDNIGHT -> Midnight
            ThemeId.CITRUS_POP -> CitrusPop
            ThemeId.CYBER_NEON -> CyberNeon
            ThemeId.PASTEL_DREAM -> PastelDream
            ThemeId.OCEAN_BLUE -> OceanBlue
            ThemeId.CUSTOM -> buildCustomTheme(preferences.customAccentHex, preferences.customIsDark)
        }
        val resolvedFont = when (preferences.textFontOverride) {
            com.example.domain.models.TextFontOverride.THEME_DEFAULT -> base.fontStyle
            com.example.domain.models.TextFontOverride.OUTFIT -> NumiFontStyle.OUTFIT
            com.example.domain.models.TextFontOverride.FREDOKA -> NumiFontStyle.FREDOKA
            com.example.domain.models.TextFontOverride.JETBRAINS_MONO -> NumiFontStyle.JETBRAINS_MONO
            com.example.domain.models.TextFontOverride.SPACE_GROTESK -> NumiFontStyle.SPACE_GROTESK
        }
        return base.copy(fontStyle = resolvedFont)
    }
}
