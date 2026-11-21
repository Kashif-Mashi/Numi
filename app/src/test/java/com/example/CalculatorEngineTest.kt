package com.example

import com.example.domain.calculator.CalculationResult
import com.example.domain.calculator.CalculatorEngine
import com.example.domain.models.AngleUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun `respects operator precedence and parentheses`() {
        val r1 = CalculatorEngine.evaluate("2+3×4")
        assertEquals(CalculationResult.Success("14", 14.0), r1)

        val r2 = CalculatorEngine.evaluate("(2+3)×4")
        assertEquals(CalculationResult.Success("20", 20.0), r2)
    }

    @Test
    fun `handles floating point precision cleanly`() {
        val result = CalculatorEngine.evaluate("0.1+0.2")
        assertTrue(result is CalculationResult.Success)
        assertEquals("0.3", (result as CalculationResult.Success).formattedValue)
    }

    @Test
    fun `handles division by zero gracefully`() {
        val result = CalculatorEngine.evaluate("42÷0")
        assertEquals(CalculationResult.Error("Can't divide by zero"), result)
    }

    @Test
    fun `evaluates scientific functions in degrees and radians`() {
        val sin30 = CalculatorEngine.evaluate("sin(30)", AngleUnit.DEG) as CalculationResult.Success
        assertEquals("0.5", sin30.formattedValue)

        val cos60 = CalculatorEngine.evaluate("cos(60)", AngleUnit.DEG) as CalculationResult.Success
        assertEquals("0.5", cos60.formattedValue)

        val sinPiHalf = CalculatorEngine.evaluate("sin(π÷2)", AngleUnit.RAD) as CalculationResult.Success
        assertEquals("1", sinPiHalf.formattedValue)

        val tan90 = CalculatorEngine.evaluate("tan(90)", AngleUnit.DEG)
        assertTrue(tan90 is CalculationResult.Error)
    }

    @Test
    fun `evaluates powers roots logarithms and factorials`() {
        assertEquals("12", (CalculatorEngine.evaluate("√(144)") as CalculationResult.Success).formattedValue)
        assertEquals("120", (CalculatorEngine.evaluate("5!") as CalculationResult.Success).formattedValue)
        assertEquals("2", (CalculatorEngine.evaluate("log(100)") as CalculationResult.Success).formattedValue)
        assertEquals("64", (CalculatorEngine.evaluate("2^3!") as CalculationResult.Success).formattedValue)
        assertEquals("0.125", (CalculatorEngine.evaluate("2^−3") as CalculationResult.Success).formattedValue)
    }

    @Test
    fun `handles domain errors cleanly`() {
        assertTrue(CalculatorEngine.evaluate("√(−4)") is CalculationResult.Error)
        assertTrue(CalculatorEngine.evaluate("log(0)") is CalculationResult.Error)
        assertTrue(CalculatorEngine.evaluate("(−3)!") is CalculationResult.Error)
    }

    @Test
    fun `extracts repeat operation for repeated equals`() {
        val repeat = CalculatorEngine.extractRepeatOperation("10+5")
        assertEquals("+", repeat?.operatorSymbol)
        assertEquals("5", repeat?.operandExpression)
    }

    @Test
    fun `toggle sign wraps and unwraps numbers cleanly`() {
        assertEquals("−42", CalculatorEngine.toggleSign("42"))
        assertEquals("42", CalculatorEngine.toggleSign("−42"))
        assertEquals("8+(−5)", CalculatorEngine.toggleSign("8+5"))
        assertEquals("8+5", CalculatorEngine.toggleSign("8+(−5)"))
    }
}
