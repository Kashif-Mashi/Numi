package com.example.domain.calculator

import com.example.domain.models.AngleUnit
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

sealed interface CalculationResult {
    data class Success(val formattedValue: String, val rawValue: Double) : CalculationResult
    data class Error(val message: String) : CalculationResult
}

data class RepeatOperation(
    val operatorSymbol: String,
    val operandExpression: String
)

object CalculatorEngine {

    private val binaryOps = setOf('+', '−', '-', '×', '*', '÷', '/', '^')
    private val prefixFunctions = listOf("sin(", "cos(", "tan(", "log(", "ln(", "√(")

    fun evaluate(expression: String, angleUnit: AngleUnit = AngleUnit.DEG): CalculationResult {
        val cleaned = expression.trim()
        if (cleaned.isEmpty()) {
            return CalculationResult.Success("0", 0.0)
        }

        val normalized = balanceParentheses(cleaned)
        return try {
            val tokens = tokenize(normalized)
            if (tokens.isEmpty()) {
                return CalculationResult.Success("0", 0.0)
            }
            val parser = Parser(tokens, angleUnit)
            val result = parser.parseExpression()
            if (parser.hasRemaining()) {
                return CalculationResult.Error("Invalid expression")
            }
            if (result.isNaN()) {
                return CalculationResult.Error("Undefined")
            }
            if (result.isInfinite()) {
                return CalculationResult.Error("Too large")
            }
            val formatted = formatNumber(result)
            CalculationResult.Success(formatted, result)
        } catch (e: DivisionByZeroException) {
            CalculationResult.Error("Can't divide by zero")
        } catch (e: DomainMathException) {
            CalculationResult.Error(e.message ?: "Undefined")
        } catch (e: SyntaxMathException) {
            CalculationResult.Error("Invalid expression")
        }
    }

    /**
     * Evaluates a live preview only when the expression has a meaningful operator/function
     * and isn't ending on an incomplete binary operator.
     */
    fun evaluateLivePreview(expression: String, angleUnit: AngleUnit = AngleUnit.DEG): String? {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return null
        val lastChar = trimmed.last()
        if (lastChar in binaryOps || lastChar == '(' || lastChar == '.') return null
        val hasOperation = trimmed.any { it in binaryOps || it == '%' || it == '²' || it == '!' || it == 'π' || it == '(' } ||
            prefixFunctions.any { trimmed.contains(it) }
        if (!hasOperation) return null

        return when (val res = evaluate(trimmed, angleUnit)) {
            is CalculationResult.Success -> res.formattedValue
            is CalculationResult.Error -> null
        }
    }

    fun extractRepeatOperation(expression: String): RepeatOperation? {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return null
        // Find the last top-level binary operator (+, −, ×, ÷, ^)
        var depth = 0
        var lastTopOpIndex = -1
        for (i in trimmed.indices) {
            when (val ch = trimmed[i]) {
                '(' -> depth++
                ')' -> depth = (depth - 1).coerceAtLeast(0)
                '+', '−', '-', '×', '*', '÷', '/', '^' -> {
                    if (depth == 0 && i > 0) {
                        val prev = trimmed[i - 1]
                        val isUnaryMinus = (ch == '-' || ch == '−') && (prev in binaryOps || prev == '(')
                        if (!isUnaryMinus) {
                            lastTopOpIndex = i
                        }
                    }
                }
            }
        }
        if (lastTopOpIndex <= 0 || lastTopOpIndex >= trimmed.lastIndex) return null
        val op = trimmed[lastTopOpIndex].toString()
        val rhs = trimmed.substring(lastTopOpIndex + 1)
        if (rhs.isBlank()) return null
        return RepeatOperation(op, rhs)
    }

    fun appendDigit(current: String, digit: String, justEvaluated: Boolean): String {
        if (justEvaluated) return digit
        if (current == "0") return digit
        val lastSegment = trailingNumberSegment(current)
        if (lastSegment == "0") {
            return current.dropLast(1) + digit
        }
        return current + digit
    }

    fun appendDecimal(current: String, justEvaluated: Boolean): String {
        if (justEvaluated || current.isEmpty()) return "0."
        val last = current.last()
        if (last in binaryOps || last == '(') return current + "0."
        if (last == ')' || last == 'π' || last == '%' || last == '²' || last == '!') {
            return current + "×0."
        }
        val segment = trailingNumberSegment(current)
        if (segment.contains('.')) return current
        return "$current."
    }

    fun appendBinaryOperator(current: String, op: String): String {
        if (current.isEmpty()) {
            return if (op == "−") "−" else "0$op"
        }
        val last = current.last()
        if (last == '(') {
            return if (op == "−") "$current−" else current
        }
        if (last in binaryOps) {
            // Avoid stripping a unary minus right after '('
            if (current.length >= 2 && current[current.length - 2] == '(' && last == '−') {
                return current
            }
            if (current == "−") return current
            return current.dropLast(1) + op
        }
        if (last == '.') {
            return current.dropLast(1) + op
        }
        return current + op
    }

    fun appendPostfix(current: String, symbol: String): String {
        if (current.isEmpty()) return current
        val last = current.last()
        if (last in binaryOps || last == '(' || last == '.') return current
        return current + symbol
    }

    fun appendFunction(current: String, funcPrefix: String, justEvaluated: Boolean): String {
        if (justEvaluated || current.isEmpty() || current == "0") {
            return funcPrefix
        }
        return current + funcPrefix
    }

    fun appendPi(current: String, justEvaluated: Boolean): String {
        if (justEvaluated || current.isEmpty() || current == "0") {
            return "π"
        }
        return current + "π"
    }

    fun appendOpenParen(current: String, justEvaluated: Boolean): String {
        if (justEvaluated || current.isEmpty() || current == "0") return "("
        return "$current("
    }

    fun appendCloseParen(current: String): String {
        if (current.isEmpty()) return current
        val openCount = current.count { it == '(' }
        val closeCount = current.count { it == ')' }
        if (openCount <= closeCount) return current
        val last = current.last()
        if (last in binaryOps || last == '(') return current
        return "$current)"
    }

    fun backspace(current: String): String {
        if (current.isEmpty()) return ""
        for (fn in prefixFunctions) {
            if (current.endsWith(fn)) {
                return current.dropLast(fn.length)
            }
        }
        return current.dropLast(1)
    }

    fun toggleSign(current: String): String {
        if (current.isEmpty() || current == "0") return "−"
        if (current == "−") return ""

        // Check if entire expression is a single negative number like "−42.5"
        if (current.startsWith("−") && current.drop(1).none { it in binaryOps || it == '(' || it == ')' }) {
            return current.drop(1)
        }
        // Check if entire expression is a single positive number like "42.5"
        if (current.none { it in binaryOps || it == '(' || it == ')' }) {
            return "−$current"
        }

        // Check if trailing token is "(−<number>)"
        val wrappedRegex = Regex("\\(−([0-9.]+)\\)$")
        val wrappedMatch = wrappedRegex.find(current)
        if (wrappedMatch != null) {
            return current.removeRange(wrappedMatch.range) + wrappedMatch.groupValues[1]
        }

        // Otherwise wrap trailing number in "(−<number>)"
        val trailingNumRegex = Regex("([0-9.]+)$")
        val numMatch = trailingNumRegex.find(current)
        if (numMatch != null) {
            return current.removeRange(numMatch.range) + "(−${numMatch.value})"
        }

        return current
    }

    private fun trailingNumberSegment(expr: String): String {
        var idx = expr.length - 1
        while (idx >= 0 && (expr[idx].isDigit() || expr[idx] == '.')) {
            idx--
        }
        return expr.substring(idx + 1)
    }

    private fun balanceParentheses(expr: String): String {
        val openCount = expr.count { it == '(' }
        val closeCount = expr.count { it == ')' }
        return if (openCount > closeCount) {
            expr + ")".repeat(openCount - closeCount)
        } else {
            expr
        }
    }

    fun formatNumber(value: Double): String {
        if (value.isNaN()) return "Undefined"
        if (value.isInfinite()) return "Too large"
        val snapped = if (abs(value) < 1e-13) 0.0 else value
        if (snapped == 0.0) return "0"

        val absVal = abs(snapped)
        // Use scientific notation for extreme magnitudes
        if (absVal >= 1e12 || (absVal > 0 && absVal < 1e-8)) {
            val bd = BigDecimal(snapped, MathContext(8, RoundingMode.HALF_UP)).stripTrailingZeros()
            return bd.toString().replace('E', 'e')
        }

        val bd = BigDecimal(snapped, MathContext(12, RoundingMode.HALF_UP))
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()
        val plain = bd.toPlainString()
        return if (plain == "-0") "0" else plain
    }

    private sealed interface Token {
        data class Number(val value: Double) : Token
        data class Op(val symbol: Char) : Token
        data class Func(val name: String) : Token
        data object Pi : Token
        data object LParen : Token
        data object RParen : Token
        data object Percent : Token
        data object Square : Token
        data object Factorial : Token
    }

    private fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        while (i < input.length) {
            val ch = input[i]
            when {
                ch.isWhitespace() -> i++
                ch.isDigit() || ch == '.' -> {
                    val start = i
                    var dotCount = 0
                    while (i < input.length && (input[i].isDigit() || input[i] == '.')) {
                        if (input[i] == '.') dotCount++
                        i++
                    }
                    if (dotCount > 1) throw SyntaxMathException()
                    val numStr = input.substring(start, i)
                    val parsed = numStr.toDoubleOrNull() ?: throw SyntaxMathException()
                    tokens.add(Token.Number(parsed))
                }
                ch == 'π' -> {
                    tokens.add(Token.Pi)
                    i++
                }
                ch == '(' -> {
                    tokens.add(Token.LParen)
                    i++
                }
                ch == ')' -> {
                    tokens.add(Token.RParen)
                    i++
                }
                ch == '%' -> {
                    tokens.add(Token.Percent)
                    i++
                }
                ch == '²' -> {
                    tokens.add(Token.Square)
                    i++
                }
                ch == '!' -> {
                    tokens.add(Token.Factorial)
                    i++
                }
                ch == '√' -> {
                    tokens.add(Token.Func("sqrt"))
                    i++
                }
                ch == '+' -> {
                    tokens.add(Token.Op('+'))
                    i++
                }
                ch == '−' || ch == '-' -> {
                    tokens.add(Token.Op('-'))
                    i++
                }
                ch == '×' || ch == '*' -> {
                    tokens.add(Token.Op('*'))
                    i++
                }
                ch == '÷' || ch == '/' -> {
                    tokens.add(Token.Op('/'))
                    i++
                }
                ch == '^' -> {
                    tokens.add(Token.Op('^'))
                    i++
                }
                ch == 'e' || ch == 'E' -> {
                    // Handle scientific notation if a previous result contained 'e'
                    if (tokens.lastOrNull() is Token.Number) {
                        val baseToken = tokens.removeAt(tokens.lastIndex) as Token.Number
                        i++
                        val sign = if (i < input.length && (input[i] == '+' || input[i] == '-' || input[i] == '−')) {
                            val s = if (input[i] == '-' || input[i] == '−') -1 else 1
                            i++
                            s
                        } else 1
                        val expStart = i
                        while (i < input.length && input[i].isDigit()) i++
                        val exp = input.substring(expStart, i).toIntOrNull() ?: throw SyntaxMathException()
                        tokens.add(Token.Number(baseToken.value * 10.0.pow(sign * exp)))
                    } else {
                        throw SyntaxMathException()
                    }
                }
                ch.isLetter() -> {
                    val start = i
                    while (i < input.length && input[i].isLetter()) i++
                    val name = input.substring(start, i).lowercase()
                    if (name !in setOf("sin", "cos", "tan", "log", "ln", "sqrt")) {
                        throw SyntaxMathException()
                    }
                    tokens.add(Token.Func(name))
                }
                else -> throw SyntaxMathException()
            }
        }
        return tokens
    }

    private class Parser(
        private val tokens: List<Token>,
        private val angleUnit: AngleUnit
    ) {
        private var pos = 0

        fun hasRemaining(): Boolean = pos < tokens.size

        private fun peek(): Token? = tokens.getOrNull(pos)

        private fun consume(): Token = tokens[pos++]

        fun parseExpression(): Double {
            var left = parseTerm()
            while (true) {
                when (val t = peek()) {
                    is Token.Op -> when (t.symbol) {
                        '+' -> {
                            consume()
                            left += parseTerm()
                        }
                        '-' -> {
                            consume()
                            left -= parseTerm()
                        }
                        else -> break
                    }
                    else -> break
                }
            }
            return left
        }

        private fun parseTerm(): Double {
            var left = parseUnary()
            while (true) {
                when (val t = peek()) {
                    is Token.Op -> when (t.symbol) {
                        '*' -> {
                            consume()
                            left *= parseUnary()
                        }
                        '/' -> {
                            consume()
                            val divisor = parseUnary()
                            if (divisor == 0.0) throw DivisionByZeroException()
                            left /= divisor
                        }
                        else -> break
                    }
                    is Token.LParen, is Token.Pi, is Token.Func, is Token.Number -> {
                        // Implicit multiplication: e.g. 2(3), 2π, (2)(3), 3sin(30)
                        left *= parseUnary()
                    }
                    else -> break
                }
            }
            return left
        }

        private fun parseUnary(): Double {
            return when (val t = peek()) {
                is Token.Op -> when (t.symbol) {
                    '+' -> {
                        consume()
                        parseUnary()
                    }
                    '-' -> {
                        consume()
                        -parseUnary()
                    }
                    else -> parsePower()
                }
                else -> parsePower()
            }
        }

        private fun parsePower(): Double {
            val base = parsePostfix()
            val next = peek()
            if (next is Token.Op && next.symbol == '^') {
                consume()
                val exponent = parseUnary()
                val result = base.pow(exponent)
                if (result.isNaN()) throw DomainMathException("Undefined")
                return result
            }
            return base
        }

        private fun parsePostfix(): Double {
            var value = parsePrimary()
            while (true) {
                when (peek()) {
                    is Token.Percent -> {
                        consume()
                        value /= 100.0
                    }
                    is Token.Square -> {
                        consume()
                        value *= value
                    }
                    is Token.Factorial -> {
                        consume()
                        value = factorial(value)
                    }
                    else -> break
                }
            }
            return value
        }

        private fun parsePrimary(): Double {
            return when (val t = consumeOrThrow()) {
                is Token.Number -> t.value
                is Token.Pi -> PI
                is Token.LParen -> {
                    val inner = parseExpression()
                    if (peek() is Token.RParen) {
                        consume()
                    }
                    inner
                }
                is Token.Func -> {
                    val arg = if (peek() is Token.LParen) {
                        consume()
                        val inner = parseExpression()
                        if (peek() is Token.RParen) consume()
                        inner
                    } else {
                        parseUnary()
                    }
                    evalFunction(t.name, arg)
                }
                else -> throw SyntaxMathException()
            }
        }

        private fun consumeOrThrow(): Token {
            if (pos >= tokens.size) throw SyntaxMathException()
            return tokens[pos++]
        }

        private fun evalFunction(name: String, arg: Double): Double {
            val radians = if (angleUnit == AngleUnit.DEG) Math.toRadians(arg) else arg
            return when (name) {
                "sin" -> sin(radians)
                "cos" -> cos(radians)
                "tan" -> {
                    if (abs(cos(radians)) < 1e-12) throw DomainMathException("Undefined")
                    tan(radians)
                }
                "log" -> {
                    if (arg <= 0.0) throw DomainMathException("Undefined")
                    log10(arg)
                }
                "ln" -> {
                    if (arg <= 0.0) throw DomainMathException("Undefined")
                    ln(arg)
                }
                "sqrt" -> {
                    if (arg < 0.0) throw DomainMathException("Undefined")
                    sqrt(arg)
                }
                else -> throw SyntaxMathException()
            }
        }

        private fun factorial(n: Double): Double {
            if (n < 0.0 || abs(n - round(n)) > 1e-9) {
                throw DomainMathException("Invalid factorial")
            }
            val intN = round(n).toInt()
            if (intN > 170) throw DomainMathException("Too large")
            var acc = 1.0
            for (k in 2..intN) {
                acc *= k
            }
            return acc
        }
    }

    private class DivisionByZeroException : RuntimeException()
    private class DomainMathException(message: String) : RuntimeException(message)
    private class SyntaxMathException : RuntimeException()
}
