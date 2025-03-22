package com.ajverma.snapclothes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ajverma.snapclothes.R

val AvenirNext = FontFamily(
    Font(R.font.avenir_next_regular, FontWeight.Normal),
    Font(R.font.avenir_next_italic, FontWeight.Medium),
    Font(R.font.avenir_next_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = AvenirNext,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AvenirNext,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AvenirNext,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AvenirNext,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AvenirNext,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)
