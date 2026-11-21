package com.example.presentation.themes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.preferences.NumiPreferencesRepository
import com.example.domain.models.NumiFontStyle
import com.example.domain.models.NumiThemeSpec
import com.example.domain.models.TextDepthStyle
import com.example.domain.models.TextFontOverride
import com.example.domain.models.TextShadowStyle
import com.example.domain.models.ThemeId
import com.example.domain.models.UserPreferences
import com.example.ui.colors.NumiPalettes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemesUiState(
    val activeThemeId: ThemeId = ThemeId.CREAM_CLASSIC,
    val previewThemeId: ThemeId = ThemeId.CREAM_CLASSIC,
    val customAccentHex: Long = 0xFF6366F1,
    val customIsDark: Boolean = false,
    val textFontOverride: TextFontOverride = TextFontOverride.THEME_DEFAULT,
    val textDepthStyle: TextDepthStyle = TextDepthStyle.MEDIUM,
    val textShadowStyle: TextShadowStyle = TextShadowStyle.SOFT,
    val numberScaleFactor: Float = 1.0f,
    val previewSpec: NumiThemeSpec = NumiPalettes.CreamClassic,
    val allThemes: List<NumiThemeSpec> = NumiPalettes.presetThemes
)

class ThemesViewModel(
    private val preferencesRepository: NumiPreferencesRepository
) : ViewModel() {

    private val previewThemeId = MutableStateFlow<ThemeId?>(null)
    private val previewCustomAccent = MutableStateFlow<Long?>(null)
    private val previewCustomDark = MutableStateFlow<Boolean?>(null)

    val uiState: StateFlow<ThemesUiState> = combine(
        preferencesRepository.preferencesFlow,
        previewThemeId,
        previewCustomAccent,
        previewCustomDark
    ) { prefs: UserPreferences, pId: ThemeId?, pAccent: Long?, pDark: Boolean? ->
        val selectedPreviewId = pId ?: prefs.themeId
        val accent = pAccent ?: prefs.customAccentHex
        val isDark = pDark ?: prefs.customIsDark
        val customSpec = NumiPalettes.buildCustomTheme(accent, isDark)
        val baseSpec = when (selectedPreviewId) {
            ThemeId.CUSTOM -> customSpec
            else -> NumiPalettes.presetThemes.firstOrNull { it.id == selectedPreviewId }
                ?: NumiPalettes.CreamClassic
        }
        val resolvedFont = when (prefs.textFontOverride) {
            TextFontOverride.THEME_DEFAULT -> baseSpec.fontStyle
            TextFontOverride.OUTFIT -> NumiFontStyle.OUTFIT
            TextFontOverride.FREDOKA -> NumiFontStyle.FREDOKA
            TextFontOverride.JETBRAINS_MONO -> NumiFontStyle.JETBRAINS_MONO
            TextFontOverride.SPACE_GROTESK -> NumiFontStyle.SPACE_GROTESK
        }
        val previewSpec = baseSpec.copy(fontStyle = resolvedFont)

        ThemesUiState(
            activeThemeId = prefs.themeId,
            previewThemeId = selectedPreviewId,
            customAccentHex = accent,
            customIsDark = isDark,
            textFontOverride = prefs.textFontOverride,
            textDepthStyle = prefs.textDepthStyle,
            textShadowStyle = prefs.textShadowStyle,
            numberScaleFactor = prefs.numberScaleFactor,
            previewSpec = previewSpec,
            allThemes = NumiPalettes.presetThemes + customSpec
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemesUiState()
    )

    fun previewTheme(themeId: ThemeId) {
        previewThemeId.value = themeId
    }

    fun previewCustomAccent(accentHex: Long) {
        previewThemeId.value = ThemeId.CUSTOM
        previewCustomAccent.value = accentHex
    }

    fun previewCustomDark(isDark: Boolean) {
        previewThemeId.value = ThemeId.CUSTOM
        previewCustomDark.value = isDark
    }

    fun setTextFontOverride(fontOverride: TextFontOverride) {
        viewModelScope.launch {
            preferencesRepository.setTextCustomization(fontOverride = fontOverride)
        }
    }

    fun setTextDepthStyle(depthStyle: TextDepthStyle) {
        viewModelScope.launch {
            preferencesRepository.setTextCustomization(depthStyle = depthStyle)
        }
    }

    fun setTextShadowStyle(shadowStyle: TextShadowStyle) {
        viewModelScope.launch {
            preferencesRepository.setTextCustomization(shadowStyle = shadowStyle)
        }
    }

    fun setNumberScaleFactor(scale: Float) {
        viewModelScope.launch {
            preferencesRepository.setTextCustomization(numberScaleFactor = scale)
        }
    }

    fun applyPreviewedTheme() {
        val state = uiState.value
        viewModelScope.launch {
            if (state.previewThemeId == ThemeId.CUSTOM) {
                preferencesRepository.setCustomThemeConfig(
                    accentHex = state.customAccentHex,
                    isDark = state.customIsDark
                )
            } else {
                preferencesRepository.setTheme(state.previewThemeId)
            }
        }
    }

    class Factory(
        private val preferencesRepository: NumiPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ThemesViewModel(preferencesRepository) as T
        }
    }
}
