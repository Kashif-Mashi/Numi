package com.example.presentation.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.animations.SoundEffectType
import com.example.data.history.HistoryRepository
import com.example.data.preferences.NumiPreferencesRepository
import com.example.domain.calculator.CalculationResult
import com.example.domain.calculator.CalculatorEngine
import com.example.domain.calculator.RepeatOperation
import com.example.domain.models.AngleUnit
import com.example.domain.models.CalcAction
import com.example.domain.models.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorUiState(
    val expression: String = "",
    val primaryDisplay: String = "0",
    val livePreview: String? = null,
    val isError: Boolean = false,
    val justEvaluated: Boolean = false,
    val isScientificExpanded: Boolean = false,
    val pulseTrigger: Int = 0,
    val lastActionCategory: SoundEffectType = SoundEffectType.DIGIT,
    val preferences: UserPreferences = UserPreferences()
)

class CalculatorViewModel(
    private val preferencesRepository: NumiPreferencesRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private data class InternalCalcState(
        val expression: String = "",
        val primaryDisplay: String = "0",
        val livePreview: String? = null,
        val isError: Boolean = false,
        val justEvaluated: Boolean = false,
        val isScientificExpanded: Boolean = false,
        val pulseTrigger: Int = 0,
        val lastActionCategory: SoundEffectType = SoundEffectType.DIGIT,
        val repeatOperation: RepeatOperation? = null
    )

    private val internalState = MutableStateFlow(InternalCalcState())

    val uiState: StateFlow<CalculatorUiState> = combine(
        internalState,
        preferencesRepository.preferencesFlow
    ) { calc, prefs ->
        CalculatorUiState(
            expression = calc.expression,
            primaryDisplay = calc.primaryDisplay,
            livePreview = calc.livePreview,
            isError = calc.isError,
            justEvaluated = calc.justEvaluated,
            isScientificExpanded = calc.isScientificExpanded,
            pulseTrigger = calc.pulseTrigger,
            lastActionCategory = calc.lastActionCategory,
            preferences = prefs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalculatorUiState()
    )

    fun toggleScientificMode() {
        internalState.update { it.copy(isScientificExpanded = !it.isScientificExpanded) }
    }

    fun toggleAngleUnit() {
        val current = uiState.value.preferences.angleUnit
        val next = if (current == AngleUnit.DEG) AngleUnit.RAD else AngleUnit.DEG
        viewModelScope.launch {
            preferencesRepository.setAngleUnit(next)
            // Re-evaluate live preview with new angle unit
            internalState.update { state ->
                val preview = CalculatorEngine.evaluateLivePreview(state.expression, next)
                state.copy(livePreview = preview)
            }
        }
    }

    fun reuseFromHistory(value: String) {
        internalState.update { state ->
            val currentExpr = if (state.isError || state.justEvaluated) "" else state.expression
            val nextExpr = if (currentExpr.isEmpty() || currentExpr == "0") {
                value
            } else {
                val last = currentExpr.last()
                if (last in setOf('+', '−', '-', '×', '*', '÷', '/', '^', '(')) {
                    currentExpr + value
                } else {
                    value
                }
            }
            val angle = uiState.value.preferences.angleUnit
            state.copy(
                expression = nextExpr,
                primaryDisplay = nextExpr,
                livePreview = CalculatorEngine.evaluateLivePreview(nextExpr, angle),
                isError = false,
                justEvaluated = false,
                repeatOperation = null,
                pulseTrigger = state.pulseTrigger + 1
            )
        }
    }

    fun onAction(action: CalcAction): SoundEffectType {
        val angleUnit = uiState.value.preferences.angleUnit
        var soundType = SoundEffectType.DIGIT

        when (action) {
            CalcAction.DIGIT_0 -> soundType = handleDigit("0", angleUnit)
            CalcAction.DIGIT_1 -> soundType = handleDigit("1", angleUnit)
            CalcAction.DIGIT_2 -> soundType = handleDigit("2", angleUnit)
            CalcAction.DIGIT_3 -> soundType = handleDigit("3", angleUnit)
            CalcAction.DIGIT_4 -> soundType = handleDigit("4", angleUnit)
            CalcAction.DIGIT_5 -> soundType = handleDigit("5", angleUnit)
            CalcAction.DIGIT_6 -> soundType = handleDigit("6", angleUnit)
            CalcAction.DIGIT_7 -> soundType = handleDigit("7", angleUnit)
            CalcAction.DIGIT_8 -> soundType = handleDigit("8", angleUnit)
            CalcAction.DIGIT_9 -> soundType = handleDigit("9", angleUnit)
            CalcAction.DECIMAL -> {
                soundType = SoundEffectType.DIGIT
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendDecimal(base, state.justEvaluated)
                }
            }
            CalcAction.ADD -> soundType = handleBinaryOp("+", angleUnit)
            CalcAction.SUBTRACT -> soundType = handleBinaryOp("−", angleUnit)
            CalcAction.MULTIPLY -> soundType = handleBinaryOp("×", angleUnit)
            CalcAction.DIVIDE -> soundType = handleBinaryOp("÷", angleUnit)
            CalcAction.POWER -> soundType = handleBinaryOp("^", angleUnit)
            CalcAction.PERCENT -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else if (state.justEvaluated) state.primaryDisplay else state.expression
                    CalculatorEngine.appendPostfix(base, "%")
                }
            }
            CalcAction.SQUARE -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else if (state.justEvaluated) state.primaryDisplay else state.expression
                    CalculatorEngine.appendPostfix(base, "²")
                }
            }
            CalcAction.FACTORIAL -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else if (state.justEvaluated) state.primaryDisplay else state.expression
                    CalculatorEngine.appendPostfix(base, "!")
                }
            }
            CalcAction.SQRT -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "√(", state.justEvaluated)
                }
            }
            CalcAction.SIN -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "sin(", state.justEvaluated)
                }
            }
            CalcAction.COS -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "cos(", state.justEvaluated)
                }
            }
            CalcAction.TAN -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "tan(", state.justEvaluated)
                }
            }
            CalcAction.LOG10 -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "log(", state.justEvaluated)
                }
            }
            CalcAction.LN -> {
                soundType = SoundEffectType.OPERATOR
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendFunction(base, "ln(", state.justEvaluated)
                }
            }
            CalcAction.PI -> {
                soundType = SoundEffectType.DIGIT
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendPi(base, state.justEvaluated)
                }
            }
            CalcAction.OPEN_PAREN -> {
                soundType = SoundEffectType.UTILITY
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendOpenParen(base, state.justEvaluated)
                }
            }
            CalcAction.CLOSE_PAREN -> {
                soundType = SoundEffectType.UTILITY
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else state.expression
                    CalculatorEngine.appendCloseParen(base)
                }
            }
            CalcAction.SIGN_TOGGLE -> {
                soundType = SoundEffectType.UTILITY
                updateExpression(angleUnit) { state ->
                    val base = if (state.isError) "" else if (state.justEvaluated) state.primaryDisplay else state.expression
                    CalculatorEngine.toggleSign(base)
                }
            }
            CalcAction.BACKSPACE -> {
                soundType = SoundEffectType.UTILITY
                internalState.update { state ->
                    if (state.isError || state.justEvaluated) {
                        InternalCalcState(
                            isScientificExpanded = state.isScientificExpanded,
                            pulseTrigger = state.pulseTrigger + 1,
                            lastActionCategory = SoundEffectType.UTILITY
                        )
                    } else {
                        val next = CalculatorEngine.backspace(state.expression)
                        state.copy(
                            expression = next,
                            primaryDisplay = next.ifEmpty { "0" },
                            livePreview = CalculatorEngine.evaluateLivePreview(next, angleUnit),
                            repeatOperation = null,
                            pulseTrigger = state.pulseTrigger + 1,
                            lastActionCategory = SoundEffectType.UTILITY
                        )
                    }
                }
            }
            CalcAction.CLEAR -> {
                soundType = SoundEffectType.UTILITY
                internalState.update { state ->
                    InternalCalcState(
                        isScientificExpanded = state.isScientificExpanded,
                        pulseTrigger = state.pulseTrigger + 1,
                        lastActionCategory = SoundEffectType.UTILITY
                    )
                }
            }
            CalcAction.EQUALS -> {
                soundType = SoundEffectType.EQUALS
                handleEquals(angleUnit)
            }
            CalcAction.ANGLE_TOGGLE -> {
                soundType = SoundEffectType.UTILITY
                toggleAngleUnit()
            }
        }
        return soundType
    }

    private fun handleDigit(digit: String, angleUnit: AngleUnit): SoundEffectType {
        updateExpression(angleUnit) { state ->
            val base = if (state.isError) "" else state.expression
            CalculatorEngine.appendDigit(base, digit, state.justEvaluated)
        }
        return SoundEffectType.DIGIT
    }

    private fun handleBinaryOp(op: String, angleUnit: AngleUnit): SoundEffectType {
        updateExpression(angleUnit) { state ->
            val base = when {
                state.isError -> "0"
                state.justEvaluated -> state.primaryDisplay
                else -> state.expression
            }
            CalculatorEngine.appendBinaryOperator(base, op)
        }
        return SoundEffectType.OPERATOR
    }

    private fun updateExpression(
        angleUnit: AngleUnit,
        transform: (InternalCalcState) -> String
    ) {
        internalState.update { state ->
            val next = transform(state)
            state.copy(
                expression = next,
                primaryDisplay = next.ifEmpty { "0" },
                livePreview = CalculatorEngine.evaluateLivePreview(next, angleUnit),
                isError = false,
                justEvaluated = false,
                repeatOperation = null,
                pulseTrigger = state.pulseTrigger + 1
            )
        }
    }

    private fun handleEquals(angleUnit: AngleUnit) {
        val current = internalState.value
        if (current.isError) return

        val exprToEvaluate: String
        val nextRepeatOp: RepeatOperation?

        if (current.justEvaluated && current.repeatOperation != null) {
            val rep = current.repeatOperation
            exprToEvaluate = "${current.primaryDisplay}${rep.operatorSymbol}${rep.operandExpression}"
            nextRepeatOp = rep
        } else {
            if (current.expression.isBlank()) return
            exprToEvaluate = current.expression
            nextRepeatOp = CalculatorEngine.extractRepeatOperation(exprToEvaluate)
        }

        when (val result = CalculatorEngine.evaluate(exprToEvaluate, angleUnit)) {
            is CalculationResult.Success -> {
                internalState.update { state ->
                    state.copy(
                        expression = exprToEvaluate,
                        primaryDisplay = result.formattedValue,
                        livePreview = null,
                        isError = false,
                        justEvaluated = true,
                        repeatOperation = nextRepeatOp,
                        pulseTrigger = state.pulseTrigger + 1,
                        lastActionCategory = SoundEffectType.EQUALS
                    )
                }
                viewModelScope.launch {
                    historyRepository.saveCalculation(exprToEvaluate, result.formattedValue)
                }
            }
            is CalculationResult.Error -> {
                internalState.update { state ->
                    state.copy(
                        expression = exprToEvaluate,
                        primaryDisplay = result.message,
                        livePreview = null,
                        isError = true,
                        justEvaluated = true,
                        repeatOperation = null,
                        pulseTrigger = state.pulseTrigger + 1,
                        lastActionCategory = SoundEffectType.EQUALS
                    )
                }
            }
        }
    }

    class Factory(
        private val preferencesRepository: NumiPreferencesRepository,
        private val historyRepository: HistoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalculatorViewModel(preferencesRepository, historyRepository) as T
        }
    }
}
