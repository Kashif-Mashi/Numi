package com.example.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animations.NumiFeedbackManager
import com.example.data.history.HistoryRepository
import com.example.data.preferences.NumiPreferencesRepository
import com.example.presentation.calculator.CalculatorScreen
import com.example.presentation.calculator.CalculatorViewModel
import com.example.presentation.history.HistoryScreen
import com.example.presentation.history.HistoryViewModel
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.settings.SettingsViewModel
import com.example.presentation.themes.ThemesScreen
import com.example.presentation.themes.ThemesViewModel

object NumiRoutes {
    const val CALCULATOR = "calculator"
    const val THEMES = "themes"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
}

@Composable
fun NumiNavGraph(
    preferencesRepository: NumiPreferencesRepository,
    historyRepository: HistoryRepository,
    feedbackManager: NumiFeedbackManager
) {
    val navController = rememberNavController()

    val calculatorViewModel: CalculatorViewModel = viewModel(
        factory = CalculatorViewModel.Factory(preferencesRepository, historyRepository)
    )

    NavHost(
        navController = navController,
        startDestination = NumiRoutes.CALCULATOR,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(260)
            ) + fadeIn(tween(220))
        },
        exitTransition = {
            fadeOut(tween(180))
        },
        popEnterTransition = {
            fadeIn(tween(220))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(240)
            ) + fadeOut(tween(180))
        }
    ) {
        composable(NumiRoutes.CALCULATOR) {
            val uiState by calculatorViewModel.uiState.collectAsStateWithLifecycle()
            CalculatorScreen(
                uiState = uiState,
                feedbackManager = feedbackManager,
                onAction = calculatorViewModel::onAction,
                onToggleScientific = calculatorViewModel::toggleScientificMode,
                onToggleAngleUnit = calculatorViewModel::toggleAngleUnit,
                onOpenThemes = { navController.navigate(NumiRoutes.THEMES) },
                onOpenHistory = { navController.navigate(NumiRoutes.HISTORY) },
                onOpenSettings = { navController.navigate(NumiRoutes.SETTINGS) }
            )
        }

        composable(NumiRoutes.THEMES) {
            val themesViewModel: ThemesViewModel = viewModel(
                factory = ThemesViewModel.Factory(preferencesRepository)
            )
            val uiState by themesViewModel.uiState.collectAsStateWithLifecycle()
            val calcUiState by calculatorViewModel.uiState.collectAsStateWithLifecycle()
            ThemesScreen(
                uiState = uiState,
                feedbackManager = feedbackManager,
                hapticsEnabled = calcUiState.preferences.hapticsEnabled,
                soundEnabled = calcUiState.preferences.soundEnabled,
                onPreviewTheme = themesViewModel::previewTheme,
                onPreviewCustomAccent = themesViewModel::previewCustomAccent,
                onPreviewCustomDark = themesViewModel::previewCustomDark,
                onSetTextFontOverride = themesViewModel::setTextFontOverride,
                onSetTextDepthStyle = themesViewModel::setTextDepthStyle,
                onSetTextShadowStyle = themesViewModel::setTextShadowStyle,
                onSetNumberScaleFactor = themesViewModel::setNumberScaleFactor,
                onApplyTheme = themesViewModel::applyPreviewedTheme,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NumiRoutes.HISTORY) {
            val historyViewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModel.Factory(historyRepository)
            )
            val entries by historyViewModel.historyEntries.collectAsStateWithLifecycle()
            HistoryScreen(
                entries = entries,
                onReuseResult = { result ->
                    calculatorViewModel.reuseFromHistory(result)
                    navController.popBackStack()
                },
                onDeleteEntry = historyViewModel::deleteEntry,
                onClearAll = historyViewModel::clearAll,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NumiRoutes.SETTINGS) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.Factory(preferencesRepository)
            )
            val prefs by settingsViewModel.preferences.collectAsStateWithLifecycle()
            SettingsScreen(
                preferences = prefs,
                feedbackManager = feedbackManager,
                onSetAnimationIntensity = settingsViewModel::setAnimationIntensity,
                onSetHapticsEnabled = settingsViewModel::setHapticsEnabled,
                onSetSoundEnabled = settingsViewModel::setSoundEnabled,
                onSetAngleUnit = settingsViewModel::setAngleUnit,
                onOpenThemes = { navController.navigate(NumiRoutes.THEMES) },
                onResetPreferences = settingsViewModel::resetPreferences,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
