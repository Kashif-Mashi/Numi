package com.example.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.NumiMotion
import com.example.domain.calculator.ExpressionFormatter
import com.example.domain.models.AngleUnit
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.TextShadowStyle
import com.example.ui.theme.LocalAnimationIntensity
import com.example.ui.theme.LocalNumiTextCustomization
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun Interactive3DNumberDisplay(
    expression: String,
    primaryDisplay: String,
    livePreview: String?,
    isError: Boolean,
    justEvaluated: Boolean,
    pulseTrigger: Int,
    angleUnit: AngleUnit,
    isScientificMode: Boolean,
    onToggleAngleUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current
    val intensity = LocalAnimationIntensity.current
    val textCustom = LocalNumiTextCustomization.current
    val density = LocalDensity.current.density
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val rotX = remember { Animatable(0f) }
    val rotY = remember { Animatable(0f) }
    val rotZ = remember { Animatable(0f) }
    val spinY = remember { Animatable(0f) }
    val pinchScale = remember { Animatable(1f) }
    val popScale = remember { Animatable(1f) }
    var isDragging by remember { mutableStateOf(false) }
    var lastTapTimeMs by remember { mutableLongStateOf(0L) }

    // Input pulse hop when buttons are pressed
    LaunchedEffect(pulseTrigger) {
        if (pulseTrigger > 0) {
            val boost = when {
                justEvaluated -> 1.095f
                intensity == AnimationIntensity.SUBTLE -> 1.02f
                intensity == AnimationIntensity.PLAYFUL -> 1.055f
                else -> 1.085f
            }
            popScale.snapTo(boost)
            popScale.animateTo(1f, NumiMotion.numberPopSpring(intensity))
        }
    }

    // Subtle idle floating wobble when user isn't dragging
    val infiniteTransition = rememberInfiniteTransition(label = "idleFloat")
    val idleWave by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleWave"
    )
    val idleTiltY by infiniteTransition.animateFloat(
        initialValue = -1.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleTiltY"
    )

    val idleWeight by animateFloatAsState(
        targetValue = if (isDragging) 0f else intensity.scale,
        animationSpec = tween(250),
        label = "idleWeight"
    )

    val formattedExpression = remember(expression) {
        if (expression.isEmpty()) "" else ExpressionFormatter.formatForDisplay(expression)
    }
    val formattedPrimary = remember(primaryDisplay, isError) {
        if (isError) primaryDisplay else ExpressionFormatter.formatForDisplay(primaryDisplay)
    }

    val expressionScroll = rememberScrollState()
    LaunchedEffect(formattedExpression) {
        expressionScroll.animateScrollTo(expressionScroll.maxValue)
    }

    val numberScroll = rememberScrollState()
    LaunchedEffect(formattedPrimary) {
        numberScroll.scrollTo(numberScroll.maxValue)
    }

    val dynamicFontSize = remember(formattedPrimary.length, isError, textCustom.numberScaleFactor) {
        val base = when {
            isError -> 28f
            formattedPrimary.length <= 7 -> 62f
            formattedPrimary.length <= 10 -> 50f
            formattedPrimary.length <= 13 -> 40f
            else -> 32f
        }
        (base * textCustom.numberScaleFactor).sp
    }

    val displayShape = RoundedCornerShape(theme.displayCornerRadius)
    val surfaceBrush = Brush.verticalGradient(
        colors = listOf(
            theme.displaySurface,
            lerp(theme.displaySurface, theme.backgroundBottom, 0.25f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (theme.isDark) 18.dp else 12.dp,
                shape = displayShape,
                ambientColor = theme.shadowColor,
                spotColor = theme.shadowColor
            )
            .clip(displayShape)
            .background(surfaceBrush)
            .drawBehind {
                // Dynamic specular sheen that follows 3D tilt angle
                val sheenX = size.width * (0.5f + (rotY.value / 84f))
                val sheenY = size.height * (0.35f - (rotX.value / 70f))
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            theme.accentColor.copy(alpha = if (isDragging) 0.14f else 0.05f),
                            theme.displaySurface.copy(alpha = 0f)
                        ),
                        center = Offset(sheenX, sheenY),
                        radius = size.maxDimension * 0.7f
                    ),
                    radius = size.maxDimension * 0.7f,
                    center = Offset(sheenX, sheenY)
                )
            }
            .border(
                width = 1.5.dp,
                color = if (isDragging) theme.accentColor.copy(alpha = 0.75f) else theme.displayBorder,
                shape = displayShape
            )
            .testTag("calculator_display_3d")
            .pointerInput(intensity) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    isDragging = true
                    var totalMovement = 0f
                    do {
                        val event = awaitPointerEvent()
                        val pressedCount = event.changes.count { it.pressed }
                        if (pressedCount > 0) {
                            val pan = event.calculatePan()
                            val zoom = event.calculateZoom()
                            val rotation = event.calculateRotation()
                            totalMovement += abs(pan.x) + abs(pan.y)

                            if (pan != Offset.Zero || zoom != 1f || rotation != 0f) {
                                event.changes.forEach { it.consume() }
                                scope.launch {
                                    val sensitivity = 0.32f
                                    val nextY = (rotY.value + pan.x * sensitivity).coerceIn(-44f, 44f)
                                    val nextX = (rotX.value - pan.y * sensitivity).coerceIn(-36f, 36f)
                                    rotY.snapTo(nextY)
                                    rotX.snapTo(nextX)

                                    if (pressedCount >= 2) {
                                        val nextScale = (pinchScale.value * zoom).coerceIn(0.84f, 1.35f)
                                        val nextZ = (rotZ.value + rotation).coerceIn(-18f, 18f)
                                        pinchScale.snapTo(nextScale)
                                        rotZ.snapTo(nextZ)
                                    }
                                }
                            }
                        }
                    } while (event.changes.any { it.pressed })

                    isDragging = false

                    // Quick double-tap triggers a playful 360-degree 3D spin!
                    if (totalMovement < 14f) {
                        val now = System.currentTimeMillis()
                        if (now - lastTapTimeMs < 320L) {
                            lastTapTimeMs = 0L
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch {
                                spinY.snapTo(0f)
                                spinY.animateTo(
                                    targetValue = 360f,
                                    animationSpec = spring(
                                        dampingRatio = 0.58f,
                                        stiffness = 220f
                                    )
                                )
                                spinY.snapTo(0f)
                            }
                        } else {
                            lastTapTimeMs = now
                        }
                    }

                    val releaseSpec = NumiMotion.releaseSpring(intensity)
                    scope.launch { rotX.animateTo(0f, releaseSpec) }
                    scope.launch { rotY.animateTo(0f, releaseSpec) }
                    scope.launch { rotZ.animateTo(0f, releaseSpec) }
                    scope.launch { pinchScale.animateTo(1f, releaseSpec) }
                }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top metadata row: live 3D angle badge + angle mode chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(theme.accentColor.copy(alpha = if (isDragging) 0.20f else 0.10f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ViewInAr,
                        contentDescription = null,
                        tint = theme.accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isDragging) {
                            "3D ${rotX.value.roundToInt()}° / ${rotY.value.roundToInt()}°"
                        } else {
                            "Drag 3D • 2×Tap Spin"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = theme.accentColor
                    )
                }

                AnimatedVisibility(
                    visible = isScientificMode || angleUnit == AngleUnit.RAD,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.utilityKeyBg)
                            .clickable(onClick = onToggleAngleUnit)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .testTag("display_angle_chip")
                    ) {
                        Text(
                            text = angleUnit.name,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = theme.utilityKeyText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expression tape row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(expressionScroll),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedExpression,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontFamily = NumiFonts.displayFamily(theme.fontStyle)
                        ),
                        color = theme.secondaryText,
                        maxLines = 1,
                        textAlign = TextAlign.End,
                        modifier = Modifier.testTag("display_expression")
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Signature Interactive 3D Extruded Number
            val activeRotX = rotX.value + (idleWave * 1.4f * idleWeight)
            val activeRotY = rotY.value + spinY.value + (idleTiltY * 1.5f * idleWeight)
            val activeRotZ = rotZ.value
            val activeScale = pinchScale.value * popScale.value
            val floatOffsetY = idleWave * 2.5f * density * idleWeight

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .padding(vertical = 4.dp)
                    .graphicsLayer {
                        cameraDistance = 12f * density
                        rotationX = activeRotX
                        rotationY = activeRotY
                        rotationZ = activeRotZ
                        scaleX = activeScale
                        scaleY = activeScale
                        translationY = floatOffsetY
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(numberScroll),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedContent(
                        targetState = formattedPrimary,
                        transitionSpec = {
                            (fadeIn(tween(130)) + scaleIn(initialScale = 0.92f)) togetherWith
                                (fadeOut(tween(90)) + scaleOut(targetScale = 1.04f))
                        },
                        label = "primaryNumberAnim"
                    ) { numberText ->
                        val textStyle = MaterialTheme.typography.displayLarge.copy(
                            fontSize = dynamicFontSize,
                            lineHeight = dynamicFontSize * 1.12f,
                            fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                            fontWeight = FontWeight.Bold
                        )

                        val depthLayers = textCustom.depthStyle.layers
                        val depthScale = textCustom.depthStyle.offsetScale

                        Box(contentAlignment = Alignment.CenterEnd) {
                            // Cast floor shadow reacting to 3D tilt & customizable shadow style
                            val floorShadowAlpha = when (textCustom.shadowStyle) {
                                TextShadowStyle.SOFT -> 0.85f
                                TextShadowStyle.CRISP -> 1.0f
                                TextShadowStyle.GLOW -> 0.55f
                            }
                            Text(
                                text = numberText,
                                style = textStyle,
                                color = theme.shadowColor.copy(alpha = theme.shadowColor.alpha * floorShadowAlpha),
                                maxLines = 1,
                                modifier = Modifier.offset {
                                    IntOffset(
                                        x = (-rotY.value * 0.48f * depthScale * density).roundToInt(),
                                        y = (rotX.value * 0.48f * depthScale * density + 7f * depthScale * density).roundToInt()
                                    )
                                }
                            )

                            // Extruded 3D thickness slices driven by user's TextDepthStyle
                            for (slice in depthLayers downTo 1) {
                                val t = slice.toFloat() / (depthLayers + 1f)
                                val sliceColor = lerp(theme.extrusionColor, theme.shadowColor, t * 0.55f)
                                Text(
                                    text = numberText,
                                    style = textStyle,
                                    color = sliceColor,
                                    maxLines = 1,
                                    modifier = Modifier.offset {
                                        IntOffset(
                                            x = (-rotY.value * 0.14f * slice * depthScale * density).roundToInt(),
                                            y = ((rotX.value * 0.14f + 1.05f) * slice * depthScale * density).roundToInt()
                                        )
                                    }
                                )
                            }

                            // Front illuminated face with customizable TextShadowStyle
                            val faceColor = when {
                                isError -> theme.secondaryAccentColor
                                justEvaluated && theme.neonGlowColor != null -> theme.neonGlowColor
                                else -> theme.primaryText
                            }
                            val customShadow = when (textCustom.shadowStyle) {
                                TextShadowStyle.SOFT -> Shadow(
                                    color = (theme.neonGlowColor ?: theme.shadowColor).copy(alpha = 0.35f),
                                    offset = Offset(0f, 3f * density),
                                    blurRadius = 10f
                                )
                                TextShadowStyle.CRISP -> Shadow(
                                    color = theme.extrusionColor.copy(alpha = 0.85f),
                                    offset = Offset(1.5f * density, 2.5f * density),
                                    blurRadius = 0.5f
                                )
                                TextShadowStyle.GLOW -> Shadow(
                                    color = (theme.neonGlowColor ?: theme.accentColor).copy(alpha = 0.65f),
                                    offset = Offset.Zero,
                                    blurRadius = 24f
                                )
                            }

                            Text(
                                text = numberText,
                                style = textStyle.copy(shadow = customShadow),
                                color = faceColor,
                                maxLines = 1,
                                modifier = Modifier
                                    .testTag("display_result")
                                    .semantics {
                                        contentDescription = "Display value $numberText"
                                    }
                            )
                        }
                    }
                }
            }

            // Live preview chip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !livePreview.isNullOrBlank() && !justEvaluated && !isError,
                    enter = fadeIn() + scaleIn(initialScale = 0.9f),
                    exit = fadeOut() + scaleOut(targetScale = 0.9f)
                ) {
                    val previewFormatted = livePreview?.let { ExpressionFormatter.formatForDisplay(it) } ?: ""
                    Text(
                        text = "= $previewFormatted",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = NumiFonts.displayFamily(theme.fontStyle)
                        ),
                        color = theme.accentColor,
                        maxLines = 1,
                        modifier = Modifier.testTag("display_live_preview")
                    )
                }
            }
        }
    }
}
