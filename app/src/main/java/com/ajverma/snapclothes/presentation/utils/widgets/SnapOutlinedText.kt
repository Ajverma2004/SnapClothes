package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SnapOutlinedText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    strokeColor: Color = Color.Black,
    style: TextStyle = TextStyle.Default,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    // Draw the "border" by placing multiple Texts offset slightly
    Box {
        for (x in -1..1) {
            for (y in -1..1) {
                if (x != 0 || y != 0) {
                    Text(
                        text = text,
                        fontWeight = fontWeight,
                        color = strokeColor,
                        style = style,
                        modifier = modifier.offset(x.dp, y.dp)
                    )
                }
            }
        }
        // Foreground text
        Text(
            modifier = modifier,
            text = text,
            style = style,
            fontWeight = fontWeight,
            color = color
        )
    }
}