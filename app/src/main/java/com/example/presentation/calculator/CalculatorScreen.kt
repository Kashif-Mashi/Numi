package com.example.presentation.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.NumiFeedbackManager
import com.example.animations.SoundEffectType
import com.example.domain.models.AngleUnit
import com.example.domain.models.ButtonCategory
import com.example.domain.models.CalcAction
import com.example.domain.models.CalculatorKey
import com.example.presentation.components.Interactive3DNumberDisplay
import com.example.presentation.components.NumiTopBar
import com.example.presentation.components.TactileCalcButton
import com.example.ui.theme.LocalNumiTheme

@Composable
fun CalculatorScreen(
    uiState: CalculatorUiState,
    feedbackManager: NumiFeedbackManager,
    onAction: (CalcAction) -> SoundEffectType,
    onToggleScientific: () -> Unit,
    onToggleAngleUnit: () -> Unit,
    onOpenThemes: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current
    val view = LocalView.current
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    val triggerPressDown: () -> Unit = {
        feedbackManager.triggerPressDownHaptic(
            view = view,
            hapticFeedback = haptic,
            hapticsEnabled = uiState.preferences.hapticsEnabled
        )
    }

    val triggerKey: (CalcAction) -> Unit = { action ->
        val soundType = onAction(action)
        feedbackManager.triggerButtonFeedback(
            view = view,
            soundEnabled = uiState.preferences.soundEnabled,
            hapticsEnabled = uiState.preferences.hapticsEnabled,
            type = soundType,
            hapticFeedback = haptic
        )
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(theme.backgroundTop, theme.backgroundBottom)
    )

    val standardRows = remember { buildStandardKeypadRows() }
    val scientificRows = remember(uiState.preferences.angleUnit) {
        buildScientificKeypadRows(uiState.preferences.angleUnit)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideLayout = maxWidth >= 620.dp && maxWidth > maxHeight

            if (isWideLayout) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.95f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NumiTopBar(
                            isScientificMode = uiState.isScientificExpanded,
                            onToggleScientific = onToggleScientific,
                            onOpenThemes = onOpenThemes,
                            onOpenAbout = onOpenAbout
                        )

                        Interactive3DNumberDisplay(
                            expression = uiState.expression,
                            primaryDisplay = uiState.primaryDisplay,
                            livePreview = uiState.livePreview,
                            isError = uiState.isError,
                            justEvaluated = uiState.justEvaluated,
                            pulseTrigger = uiState.pulseTrigger,
                            angleUnit = uiState.preferences.angleUnit,
                            isScientificMode = uiState.isScientificExpanded,
                            onToggleAngleUnit = onToggleAngleUnit,
                            modifier = Modifier.weight(1f)
                        )

                        AnimatedVisibility(
                            visible = uiState.isScientificExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            ScientificKeypad(
                                rows = scientificRows,
                                onPressDown = triggerPressDown,
                                onKeyClick = triggerKey
                            )
                        }
                    }

                    StandardKeypad(
                        rows = standardRows,
                        isScientificMode = uiState.isScientificExpanded,
                        onPressDown = triggerPressDown,
                        onKeyClick = triggerKey,
                        modifier = Modifier
                            .weight(1.05f)
                            .fillMaxHeight()
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(if (uiState.isScientificExpanded) 8.dp else 12.dp)
                ) {
                    NumiTopBar(
                        isScientificMode = uiState.isScientificExpanded,
                        onToggleScientific = onToggleScientific,
                        onOpenThemes = onOpenThemes,
                        onOpenAbout = onOpenAbout
                    )

                    Interactive3DNumberDisplay(
                        expression = uiState.expression,
                        primaryDisplay = uiState.primaryDisplay,
                        livePreview = uiState.livePreview,
                        isError = uiState.isError,
                        justEvaluated = uiState.justEvaluated,
                        pulseTrigger = uiState.pulseTrigger,
                        angleUnit = uiState.preferences.angleUnit,
                        isScientificMode = uiState.isScientificExpanded,
                        onToggleAngleUnit = onToggleAngleUnit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(if (uiState.isScientificExpanded) 0.32f else 0.38f)
                    )

                    AnimatedVisibility(
                        visible = uiState.isScientificExpanded,
                        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                    ) {
                        ScientificKeypad(
                            rows = scientificRows,
                            onPressDown = triggerPressDown,
                            onKeyClick = triggerKey
                        )
                    }

                    StandardKeypad(
                        rows = standardRows,
                        isScientificMode = uiState.isScientificExpanded,
                        onPressDown = triggerPressDown,
                        onKeyClick = triggerKey,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(if (uiState.isScientificExpanded) 0.48f else 0.62f)
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}

@Composable
private fun StandardKeypad(
    rows: List<List<CalculatorKey>>,
    isScientificMode: Boolean,
    onPressDown: () -> Unit,
    onKeyClick: (CalcAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = if (isScientificMode) 8.dp else 12.dp
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEach { key ->
                    TactileCalcButton(
                        key = key,
                        onPressDown = onPressDown,
                        onClick = { onKeyClick(key.action) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun ScientificKeypad(
    rows: List<List<CalculatorKey>>,
    onPressDown: () -> Unit,
    onKeyClick: (CalcAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { key ->
                    TactileCalcButton(
                        key = key,
                        onPressDown = onPressDown,
                        onClick = { onKeyClick(key.action) },
                        fontSizeOverride = 13.sp,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    )
                }
            }
        }
    }
}

private fun buildStandardKeypadRows(): List<List<CalculatorKey>> = listOf(
    listOf(
        CalculatorKey("C", CalcAction.CLEAR, ButtonCategory.UTILITY, "Clear all", "key_clear"),
        CalculatorKey("+/−", CalcAction.SIGN_TOGGLE, ButtonCategory.UTILITY, "Toggle sign", "key_sign"),
        CalculatorKey("%", CalcAction.PERCENT, ButtonCategory.UTILITY, "Percent", "key_percent"),
        CalculatorKey("÷", CalcAction.DIVIDE, ButtonCategory.OPERATOR, "Divide", "key_divide")
    ),
    listOf(
        CalculatorKey("7", CalcAction.DIGIT_7, ButtonCategory.DIGIT, "7", "key_7"),
        CalculatorKey("8", CalcAction.DIGIT_8, ButtonCategory.DIGIT, "8", "key_8"),
        CalculatorKey("9", CalcAction.DIGIT_9, ButtonCategory.DIGIT, "9", "key_9"),
        CalculatorKey("×", CalcAction.MULTIPLY, ButtonCategory.OPERATOR, "Multiply", "key_multiply")
    ),
    listOf(
        CalculatorKey("4", CalcAction.DIGIT_4, ButtonCategory.DIGIT, "4", "key_4"),
        CalculatorKey("5", CalcAction.DIGIT_5, ButtonCategory.DIGIT, "5", "key_5"),
        CalculatorKey("6", CalcAction.DIGIT_6, ButtonCategory.DIGIT, "6", "key_6"),
        CalculatorKey("−", CalcAction.SUBTRACT, ButtonCategory.OPERATOR, "Subtract", "key_subtract")
    ),
    listOf(
        CalculatorKey("1", CalcAction.DIGIT_1, ButtonCategory.DIGIT, "1", "key_1"),
        CalculatorKey("2", CalcAction.DIGIT_2, ButtonCategory.DIGIT, "2", "key_2"),
        CalculatorKey("3", CalcAction.DIGIT_3, ButtonCategory.DIGIT, "3", "key_3"),
        CalculatorKey("+", CalcAction.ADD, ButtonCategory.OPERATOR, "Add", "key_add")
    ),
    listOf(
        CalculatorKey("0", CalcAction.DIGIT_0, ButtonCategory.DIGIT, "0", "key_0"),
        CalculatorKey(".", CalcAction.DECIMAL, ButtonCategory.DIGIT, "Decimal point", "key_decimal"),
        CalculatorKey("⌫", CalcAction.BACKSPACE, ButtonCategory.UTILITY, "Backspace", "key_backspace"),
        CalculatorKey("=", CalcAction.EQUALS, ButtonCategory.EQUALS, "Equals", "key_equals")
    )
)

private fun buildScientificKeypadRows(angleUnit: AngleUnit): List<List<CalculatorKey>> = listOf(
    listOf(
        CalculatorKey("sin", CalcAction.SIN, ButtonCategory.SCIENTIFIC, "Sine ($angleUnit)", "key_sin"),
        CalculatorKey("cos", CalcAction.COS, ButtonCategory.SCIENTIFIC, "Cosine ($angleUnit)", "key_cos"),
        CalculatorKey("tan", CalcAction.TAN, ButtonCategory.SCIENTIFIC, "Tangent ($angleUnit)", "key_tan"),
        CalculatorKey("π", CalcAction.PI, ButtonCategory.SCIENTIFIC, "Pi", "key_pi")
    ),
    listOf(
        CalculatorKey("√", CalcAction.SQRT, ButtonCategory.SCIENTIFIC, "Square root", "key_sqrt"),
        CalculatorKey("x²", CalcAction.SQUARE, ButtonCategory.SCIENTIFIC, "Square", "key_square"),
        CalculatorKey("xʸ", CalcAction.POWER, ButtonCategory.SCIENTIFIC, "Power", "key_power"),
        CalculatorKey("n!", CalcAction.FACTORIAL, ButtonCategory.SCIENTIFIC, "Factorial", "key_factorial")
    ),
    listOf(
        CalculatorKey("(", CalcAction.OPEN_PAREN, ButtonCategory.SCIENTIFIC, "Open parenthesis", "key_open_paren"),
        CalculatorKey(")", CalcAction.CLOSE_PAREN, ButtonCategory.SCIENTIFIC, "Close parenthesis", "key_close_paren"),
        CalculatorKey("log", CalcAction.LOG10, ButtonCategory.SCIENTIFIC, "Logarithm base 10", "key_log"),
        CalculatorKey("ln", CalcAction.LN, ButtonCategory.SCIENTIFIC, "Natural logarithm", "key_ln")
    )
)
