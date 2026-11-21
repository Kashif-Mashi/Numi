package com.example.domain.models

enum class ButtonCategory {
    DIGIT,
    OPERATOR,
    UTILITY,
    EQUALS,
    SCIENTIFIC
}

enum class CalcAction {
    DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
    DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    DECIMAL,
    ADD, SUBTRACT, MULTIPLY, DIVIDE, PERCENT,
    SIGN_TOGGLE, CLEAR, BACKSPACE, EQUALS,
    // Scientific actions
    SQRT, SQUARE, POWER, OPEN_PAREN, CLOSE_PAREN,
    SIN, COS, TAN, LOG10, LN, PI, FACTORIAL, ANGLE_TOGGLE
}

data class CalculatorKey(
    val label: String,
    val action: CalcAction,
    val category: ButtonCategory,
    val contentDescription: String,
    val testTag: String
)
