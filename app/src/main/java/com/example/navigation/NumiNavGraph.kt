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
import com.example.presentation.about.AboutScreen
import com.example.presentation.calculator.CalculatorScreen
import com.example.presentation.calculator.CalculatorViewModel
import com.example.presentation.splash.SplashScreen
import com.example.presentation.themes.ThemesScreen
import com.example.presentation.themes.ThemesViewModel

object NumiRoutes {
    const val SPLASH = "splash"
    const val CALCULATOR = "calculator"
    const val THEMES = "themes"
    const val ABOUT = "about"
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
        startDestination = NumiRoutes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(240)
            ) + fadeIn(tween(200))
        },
        exitTransition = {
            fadeOut(tween(180))
        },
        popEnterTransition = {
            fadeIn(tween(200))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(240)
            ) + fadeOut(tween(180))
        }
    ) {
        composable(
            route = NumiRoutes.SPLASH,
            exitTransition = {
                fadeOut(tween(280))
            }
        ) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(NumiRoutes.CALCULATOR) {
                        popUpTo(NumiRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NumiRoutes.CALCULATOR) {
            val uiState by calculatorViewModel.uiState.collectAsStateWithLifecycle()
            CalculatorScreen(
                uiState = uiState,
                feedbackManager = feedbackManager,
                onAction = calculatorViewModel::onAction,
                onToggleScientific = calculatorViewModel::toggleScientificMode,
                onToggleAngleUnit = calculatorViewModel::toggleAngleUnit,
                onOpenThemes = { navController.navigate(NumiRoutes.THEMES) },
                onOpenAbout = { navController.navigate(NumiRoutes.ABOUT) }
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

        composable(NumiRoutes.ABOUT) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
