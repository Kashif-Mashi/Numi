package com.example.domain.calculator

object ExpressionFormatter {

    /**
     * Adds thousand-separator commas to integer portions of numbers inside expressions or results
     * without changing mathematical meaning.
     */
    fun formatForDisplay(raw: String): String {
        if (raw.isEmpty()) return "0"
        if (raw.contains('e') && raw.none { it == '(' || it == 'π' }) {
            // Keep scientific notation clean without inserting commas into exponents
            return raw
        }
        val regex = Regex("(?<![.0-9])(\\d+)(\\.\\d*)?")
        return regex.replace(raw) { match ->
            val intPart = match.groupValues[1]
            val fracPart = match.groupValues[2]
            val grouped = addThousandsCommas(intPart)
            grouped + fracPart
        }
    }

    private fun addThousandsCommas(digits: String): String {
        if (digits.length <= 3) return digits
        val sb = StringBuilder()
        var count = 0
        for (i in digits.length - 1 downTo 0) {
            sb.append(digits[i])
            count++
            if (count % 3 == 0 && i != 0) {
                sb.append(',')
            }
        }
        return sb.reverse().toString()
    }
}
