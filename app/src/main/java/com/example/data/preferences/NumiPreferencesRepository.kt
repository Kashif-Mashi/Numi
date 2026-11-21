package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.models.AngleUnit
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.TextDepthStyle
import com.example.domain.models.TextFontOverride
import com.example.domain.models.TextShadowStyle
import com.example.domain.models.ThemeId
import com.example.domain.models.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.numiDataStore: DataStore<Preferences> by preferencesDataStore(name = "numi_preferences")

class NumiPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val THEME_ID = stringPreferencesKey("theme_id")
        val ANIMATION_INTENSITY = stringPreferencesKey("animation_intensity")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val ANGLE_UNIT = stringPreferencesKey("angle_unit")
        val CUSTOM_ACCENT_HEX = longPreferencesKey("custom_accent_hex")
        val CUSTOM_IS_DARK = booleanPreferencesKey("custom_is_dark")
        val TEXT_FONT_OVERRIDE = stringPreferencesKey("text_font_override")
        val TEXT_DEPTH_STYLE = stringPreferencesKey("text_depth_style")
        val TEXT_SHADOW_STYLE = stringPreferencesKey("text_shadow_style")
        val NUMBER_SCALE_FACTOR = floatPreferencesKey("number_scale_factor")
    }

    val preferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val themeKey = prefs[Keys.THEME_ID] ?: ThemeId.CREAM_CLASSIC.storageKey
            val themeId = ThemeId.entries.firstOrNull { it.storageKey == themeKey } ?: ThemeId.CREAM_CLASSIC

            val intensityName = prefs[Keys.ANIMATION_INTENSITY] ?: AnimationIntensity.PLAYFUL.name
            val intensity = runCatching { AnimationIntensity.valueOf(intensityName) }
                .getOrDefault(AnimationIntensity.PLAYFUL)

            val angleName = prefs[Keys.ANGLE_UNIT] ?: AngleUnit.DEG.name
            val angleUnit = runCatching { AngleUnit.valueOf(angleName) }
                .getOrDefault(AngleUnit.DEG)

            val fontOverride = runCatching {
                TextFontOverride.valueOf(prefs[Keys.TEXT_FONT_OVERRIDE] ?: TextFontOverride.THEME_DEFAULT.name)
            }.getOrDefault(TextFontOverride.THEME_DEFAULT)

            val depthStyle = runCatching {
                TextDepthStyle.valueOf(prefs[Keys.TEXT_DEPTH_STYLE] ?: TextDepthStyle.MEDIUM.name)
            }.getOrDefault(TextDepthStyle.MEDIUM)

            val shadowStyle = runCatching {
                TextShadowStyle.valueOf(prefs[Keys.TEXT_SHADOW_STYLE] ?: TextShadowStyle.SOFT.name)
            }.getOrDefault(TextShadowStyle.SOFT)

            val scaleFactor = (prefs[Keys.NUMBER_SCALE_FACTOR] ?: 1.0f).coerceIn(0.85f, 1.15f)

            UserPreferences(
                themeId = themeId,
                animationIntensity = intensity,
                hapticsEnabled = prefs[Keys.HAPTICS_ENABLED] ?: true,
                soundEnabled = prefs[Keys.SOUND_ENABLED] ?: false,
                angleUnit = angleUnit,
                customAccentHex = prefs[Keys.CUSTOM_ACCENT_HEX] ?: 0xFF6366F1,
                customIsDark = prefs[Keys.CUSTOM_IS_DARK] ?: false,
                textFontOverride = fontOverride,
                textDepthStyle = depthStyle,
                textShadowStyle = shadowStyle,
                numberScaleFactor = scaleFactor
            )
        }

    suspend fun setTheme(themeId: ThemeId) {
        dataStore.edit { prefs ->
            prefs[Keys.THEME_ID] = themeId.storageKey
        }
    }

    suspend fun setCustomThemeConfig(accentHex: Long, isDark: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.CUSTOM_ACCENT_HEX] = accentHex
            prefs[Keys.CUSTOM_IS_DARK] = isDark
            prefs[Keys.THEME_ID] = ThemeId.CUSTOM.storageKey
        }
    }

    suspend fun setAnimationIntensity(intensity: AnimationIntensity) {
        dataStore.edit { prefs ->
            prefs[Keys.ANIMATION_INTENSITY] = intensity.name
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setAngleUnit(unit: AngleUnit) {
        dataStore.edit { prefs ->
            prefs[Keys.ANGLE_UNIT] = unit.name
        }
    }

    suspend fun setTextCustomization(
        fontOverride: TextFontOverride? = null,
        depthStyle: TextDepthStyle? = null,
        shadowStyle: TextShadowStyle? = null,
        numberScaleFactor: Float? = null
    ) {
        dataStore.edit { prefs ->
            fontOverride?.let { prefs[Keys.TEXT_FONT_OVERRIDE] = it.name }
            depthStyle?.let { prefs[Keys.TEXT_DEPTH_STYLE] = it.name }
            shadowStyle?.let { prefs[Keys.TEXT_SHADOW_STYLE] = it.name }
            numberScaleFactor?.let { prefs[Keys.NUMBER_SCALE_FACTOR] = it.coerceIn(0.85f, 1.15f) }
        }
    }

    suspend fun resetPreferences() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
