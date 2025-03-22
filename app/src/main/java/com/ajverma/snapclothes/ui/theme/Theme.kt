package com.ajverma.snapclothes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SnapClothesColorScheme = lightColorScheme(
    primary = SnapYellow,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun SnapClothesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SnapClothesColorScheme,
        typography = AppTypography,
        content = content
    )
}
