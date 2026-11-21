package com.example.animations

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import com.example.domain.models.AnimationIntensity

object NumiMotion {

    fun releaseSpring(intensity: AnimationIntensity): AnimationSpec<Float> {
        return when (intensity) {
            AnimationIntensity.SUBTLE -> spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
            AnimationIntensity.PLAYFUL -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
            AnimationIntensity.ENERGETIC -> spring(
                dampingRatio = 0.42f,
                stiffness = Spring.StiffnessLow
            )
        }
    }

    fun buttonSpring(intensity: AnimationIntensity): AnimationSpec<Float> {
        return when (intensity) {
            AnimationIntensity.SUBTLE -> spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
            AnimationIntensity.PLAYFUL -> spring(
                dampingRatio = 0.5f,
                stiffness = Spring.StiffnessMedium
            )
            AnimationIntensity.ENERGETIC -> spring(
                dampingRatio = 0.38f,
                stiffness = Spring.StiffnessMediumLow
            )
        }
    }

    fun numberPopSpring(intensity: AnimationIntensity): AnimationSpec<Float> {
        return when (intensity) {
            AnimationIntensity.SUBTLE -> tween(durationMillis = 140)
            AnimationIntensity.PLAYFUL -> spring(
                dampingRatio = 0.55f,
                stiffness = Spring.StiffnessMedium
            )
            AnimationIntensity.ENERGETIC -> spring(
                dampingRatio = 0.40f,
                stiffness = Spring.StiffnessMediumLow
            )
        }
    }

    fun themeColorSpec(): AnimationSpec<Color> = tween(durationMillis = 360)
}
