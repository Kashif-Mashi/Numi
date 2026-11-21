package com.example.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.preferences.NumiPreferencesRepository
import com.example.domain.models.AngleUnit
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: NumiPreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun setAnimationIntensity(intensity: AnimationIntensity) {
        viewModelScope.launch {
            preferencesRepository.setAnimationIntensity(intensity)
        }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setHapticsEnabled(enabled)
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSoundEnabled(enabled)
        }
    }

    fun setAngleUnit(unit: AngleUnit) {
        viewModelScope.launch {
            preferencesRepository.setAngleUnit(unit)
        }
    }

    fun resetPreferences() {
        viewModelScope.launch {
            preferencesRepository.resetPreferences()
        }
    }

    class Factory(
        private val preferencesRepository: NumiPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(preferencesRepository) as T
        }
    }
}
