package com.ajverma.snapclothes.presentation.utils.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun String.colorExtractor(
    modifier: Modifier = Modifier,
): Color {
    return when (this) {
        "Black" -> Color(0xFF000000)
        "Army Green", "Military green" -> Color(0xFF4B5320)
        "Brown" -> Color(0xFF8B4513)
        "Khaki" -> Color(0xFFF0E68C)
        "White" -> Color(0xFFFFFFFF)
        "Navy Blue", "Navy blue" -> Color(0xFF000080)
        "Dark Grey" -> Color(0xFFA9A9A9)
        "Dark Pink" -> Color(0xFFFF1493)
        "Rose Red" -> Color(0xFFC21E56)
        "Dark Green" -> Color(0xFF006400)
        "Orange-red" -> Color(0xFFFF4500)
        "Royal Blue" -> Color(0xFF4169E1)
        "Dark Red" -> Color(0xFF8B0000)
        "Peacock Blue" -> Color(0xFF1C39BB)
        "Light Green" -> Color(0xFF90EE90)
        "Rose Purple" -> Color(0xFFB03060)

        // Newly added colors
        "Sunflower" -> Color(0xFFFFDA03)
        "Camo" -> Color(0xFF78866B)
        "Red" -> Color(0xFFFF0000)
        "Silver" -> Color(0xFFC0C0C0)
        "Olive" -> Color(0xFF808000)
        "Light Pastel" -> Color(0xFFE6E6FA)
        "Stone" -> Color(0xFF837E7C)

        else -> Color.Black
    }
}
