package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.UserPreferences
import com.example.navigation.NumiNavGraph
import com.example.ui.colors.NumiPalettes
import com.example.ui.theme.NumiTextCustomization
import com.example.ui.theme.NumiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NumiApplication

        setContent {
            val preferences by app.preferencesRepository.preferencesFlow
                .collectAsStateWithLifecycle(initialValue = UserPreferences())

            val themeSpec = NumiPalettes.resolveTheme(preferences)
            val textCustomization = NumiTextCustomization(
                depthStyle = preferences.textDepthStyle,
                shadowStyle = preferences.textShadowStyle,
                numberScaleFactor = preferences.numberScaleFactor
            )

            LaunchedEffect(themeSpec.isDark) {
                if (themeSpec.isDark) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
                        navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
                    )
                } else {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                        navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                    )
                }
            }

            NumiTheme(
                themeSpec = themeSpec,
                animationIntensity = preferences.animationIntensity,
                textCustomization = textCustomization
            ) {
                NumiNavGraph(
                    preferencesRepository = app.preferencesRepository,
                    historyRepository = app.historyRepository,
                    feedbackManager = app.feedbackManager
                )
            }
        }
    }
}
