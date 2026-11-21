package com.example.ui.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.models.NumiFontStyle

object NumiFonts {
    val OutfitFamily = FontFamily(Font(R.font.outfit, FontWeight.Normal))
    val FredokaFamily = FontFamily(Font(R.font.fredoka, FontWeight.Normal))
    val JetBrainsMonoFamily = FontFamily(Font(R.font.jetbrains_mono, FontWeight.Normal))
    val SpaceGroteskFamily = FontFamily(Font(R.font.space_grotesk, FontWeight.Normal))
    val PlusJakartaSansFamily = FontFamily(Font(R.font.plus_jakarta_sans, FontWeight.Normal))

    fun displayFamily(style: NumiFontStyle): FontFamily = when (style) {
        NumiFontStyle.OUTFIT -> OutfitFamily
        NumiFontStyle.FREDOKA -> FredokaFamily
        NumiFontStyle.JETBRAINS_MONO -> JetBrainsMonoFamily
        NumiFontStyle.SPACE_GROTESK -> SpaceGroteskFamily
    }
}

fun buildNumiTypography(fontStyle: NumiFontStyle): Typography {
    val display = NumiFonts.displayFamily(fontStyle)
    val uiBody = NumiFonts.PlusJakartaSansFamily

    return Typography(
        displayLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 62.sp,
            lineHeight = 68.sp,
            letterSpacing = (-1.5).sp
        ),
        displayMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp,
            lineHeight = 50.sp,
            letterSpacing = (-0.8).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.25).sp
        ),
        titleLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = uiBody,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = uiBody,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = uiBody,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelLarge = TextStyle(
            fontFamily = display,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = uiBody,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    )
}
