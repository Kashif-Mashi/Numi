package com.example.domain.models

enum class AngleUnit {
    DEG,
    RAD
}

enum class AnimationIntensity(val label: String, val description: String, val scale: Float) {
    SUBTLE("Subtle", "Calm, minimal motion", 0.5f),
    PLAYFUL("Playful", "Balanced springy bounce", 1.0f),
    ENERGETIC("Energetic", "Extra lively 3D depth & pop", 1.35f)
}

enum class TextFontOverride(val label: String) {
    THEME_DEFAULT("Theme"),
    OUTFIT("Outfit"),
    FREDOKA("Fredoka"),
    JETBRAINS_MONO("Mono"),
    SPACE_GROTESK("Grotesk")
}

enum class TextDepthStyle(val label: String, val layers: Int, val offsetScale: Float) {
    SUBTLE("Soft", 2, 0.55f),
    MEDIUM("3D", 5, 1.0f),
    DEEP("Deep 3D", 8, 1.45f)
}

enum class TextShadowStyle(val label: String) {
    SOFT("Soft Drop"),
    CRISP("Crisp Edge"),
    GLOW("Luminous")
}

enum class ThemeId(val storageKey: String, val displayName: String, val tagline: String) {
    CREAM_CLASSIC("cream_classic", "Cream Classic", "Warm paper & terracotta"),
    MIDNIGHT("midnight", "Midnight", "Obsidian matte & amber"),
    CITRUS_POP("citrus_pop", "Citrus Pop", "Sunlit tangerine & lemon"),
    CYBER_NEON("cyber_neon", "Cyber Neon", "Electric cyan & matrix glow"),
    PASTEL_DREAM("pastel_dream", "Pastel Dream", "Cloud lavender & mint"),
    OCEAN_BLUE("ocean_blue", "Ocean Blue", "Deep abyss & seafoam"),
    CUSTOM("custom", "Custom Studio", "Your signature accent")
}

data class UserPreferences(
    val themeId: ThemeId = ThemeId.CREAM_CLASSIC,
    val animationIntensity: AnimationIntensity = AnimationIntensity.PLAYFUL,
    val hapticsEnabled: Boolean = true,
    val soundEnabled: Boolean = false,
    val angleUnit: AngleUnit = AngleUnit.DEG,
    val customAccentHex: Long = 0xFF6366F1,
    val customIsDark: Boolean = false,
    val textFontOverride: TextFontOverride = TextFontOverride.THEME_DEFAULT,
    val textDepthStyle: TextDepthStyle = TextDepthStyle.MEDIUM,
    val textShadowStyle: TextShadowStyle = TextShadowStyle.SOFT,
    val numberScaleFactor: Float = 1.0f
)
