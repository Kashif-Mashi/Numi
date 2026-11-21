package com.example.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.NumiMotion
import com.example.domain.models.ButtonCategory
import com.example.domain.models.CalculatorKey
import com.example.ui.theme.LocalAnimationIntensity
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts
import kotlin.math.roundToInt

@Composable
fun TactileCalcButton(
    key: CalculatorKey,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPressDown: (() -> Unit)? = null,
    fontSizeOverride: TextUnit? = null
) {
    val theme = LocalNumiTheme.current
    val intensity = LocalAnimationIntensity.current
    val densityObj = LocalDensity.current
    val density = densityObj.density
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Trigger immediate down-stroke haptic feedback the instant a finger touches the keycap
    LaunchedEffect(interactionSource, onPressDown) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                onPressDown?.invoke()
            }
        }
    }

    val springSpec = NumiMotion.buttonSpring(intensity)
    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = springSpec,
        label = "keyPress"
    )

    val (bgColor, textColor, bevelColor) = when (key.category) {
        ButtonCategory.DIGIT -> Triple(theme.digitKeyBg, theme.digitKeyText, theme.digitKeyBevel)
        ButtonCategory.OPERATOR -> Triple(theme.operatorKeyBg, theme.operatorKeyText, theme.operatorKeyBevel)
        ButtonCategory.UTILITY -> Triple(theme.utilityKeyBg, theme.utilityKeyText, theme.utilityKeyBevel)
        ButtonCategory.EQUALS -> Triple(theme.equalsKeyBg, theme.equalsKeyText, theme.equalsKeyBevel)
        ButtonCategory.SCIENTIFIC -> Triple(theme.scientificKeyBg, theme.scientificKeyText, theme.scientificKeyBevel)
    }

    val cornerRadius = when (key.category) {
        ButtonCategory.EQUALS -> theme.keyCornerRadius + 4.dp
        ButtonCategory.SCIENTIFIC -> (theme.keyCornerRadius - 4.dp).coerceAtLeast(12.dp)
        else -> theme.keyCornerRadius
    }
    val shape = RoundedCornerShape(cornerRadius)

    val bevelDepthPx = with(densityObj) { theme.keyElevationDepth.toPx() }
    val pressScale = when (key.category) {
        ButtonCategory.EQUALS -> 1f - (0.09f * pressProgress)
        ButtonCategory.OPERATOR -> 1f - (0.075f * pressProgress)
        else -> 1f - (0.06f * pressProgress)
    }
    val tiltZ = when (key.category) {
        ButtonCategory.OPERATOR -> 2.8f * intensity.scale * pressProgress
        ButtonCategory.EQUALS -> -2.2f * intensity.scale * pressProgress
        else -> 0f
    }
    val tiltX = -6f * intensity.scale * pressProgress

    val faceBrush = Brush.verticalGradient(
        colors = listOf(
            lerp(bgColor, theme.highlightColor, if (theme.isDark) 0.10f else 0.34f),
            lerp(bgColor, bevelColor, 0.12f * pressProgress)
        )
    )

    val borderStrokeColor = if (theme.neonGlowColor != null &&
        (key.category == ButtonCategory.EQUALS || key.category == ButtonCategory.OPERATOR)
    ) {
        theme.neonGlowColor.copy(alpha = 0.5f + 0.35f * pressProgress)
    } else {
        theme.highlightColor.copy(alpha = if (theme.isDark) 0.16f else 0.72f)
    }

    val defaultTextSize = when (key.category) {
        ButtonCategory.SCIENTIFIC -> 15.sp
        ButtonCategory.OPERATOR, ButtonCategory.EQUALS -> 28.sp
        ButtonCategory.UTILITY -> 22.sp
        ButtonCategory.DIGIT -> 26.sp
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .graphicsLayer {
                cameraDistance = 8f * density
                scaleX = pressScale
                scaleY = pressScale
                rotationX = tiltX
                rotationZ = tiltZ
            }
            .testTag(key.testTag)
            .semantics {
                contentDescription = key.contentDescription
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
    ) {
        // Bottom 3D keycap bevel / shadow base
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, bevelDepthPx.roundToInt()) }
                .shadow(
                    elevation = ((1f - pressProgress) * 7f).coerceAtLeast(0.5f).dp,
                    shape = shape,
                    ambientColor = theme.shadowColor,
                    spotColor = theme.shadowColor
                )
                .clip(shape)
                .background(bevelColor)
        )

        // Top keycap face that physically sinks onto the bevel with a subtle tactile press flash
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(0, (pressProgress * bevelDepthPx * 0.92f).roundToInt())
                }
                .clip(shape)
                .background(faceBrush)
                .drawWithContent {
                    drawContent()
                    if (pressProgress > 0.02f) {
                        drawCircle(
                            color = theme.highlightColor.copy(alpha = (if (theme.isDark) 0.16f else 0.25f) * pressProgress),
                            radius = size.maxDimension * (0.45f + 0.25f * pressProgress),
                            center = center
                        )
                    }
                }
                .border(1.dp, borderStrokeColor, shape)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            val labelGlow = if (theme.neonGlowColor != null && key.category == ButtonCategory.EQUALS) {
                Shadow(color = theme.neonGlowColor.copy(alpha = 0.45f), offset = Offset.Zero, blurRadius = 12f)
            } else null

            Text(
                text = key.label,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontSize = fontSizeOverride ?: defaultTextSize,
                    fontWeight = if (key.category == ButtonCategory.SCIENTIFIC) FontWeight.SemiBold else FontWeight.Bold,
                    shadow = labelGlow
                ),
                color = textColor,
                maxLines = 1
            )
        }
    }
}
