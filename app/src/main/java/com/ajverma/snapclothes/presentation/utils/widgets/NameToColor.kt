package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.ajverma.snapclothes.presentation.utils.mapper.colorExtractor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NameToColor(
    modifier: Modifier = Modifier,
    colorList: List<String>,
) {
    // Select first color by default
    val initialColor = remember { colorList.distinct().firstOrNull() }
    var selectedColor by remember { mutableStateOf(initialColor) }

    Column(modifier = modifier.padding(16.dp)) {

        // Always show color name, left aligned
        Text(
            text = buildAnnotatedString {
                append("Colour: ")

                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(selectedColor ?: "N/A")
                }
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.Start),
            color = Color.Black
        )


        // Flow layout for wrap support
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            colorList.distinct().forEach { colorName ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 8.dp)
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            color = if (colorName == selectedColor) Color.Black else Color.Transparent,
                            shape = CircleShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(40.dp)
                            .shadow(
                                3.dp,
                                shape = CircleShape
                            )
                            .background(
                                color = colorName.colorExtractor(),
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            )
                            { selectedColor = colorName }
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    NameToColor(
        colorList = listOf(
            "Black", "Army Green", "Brown", "Khaki", "White", "Navy Blue", "Dark Grey",
            "Dark Pink", "Rose Red", "Dark Green", "Orange-red", "Royal Blue",
            "Dark Red", "Peacock Blue", "Light Green", "Rose Purple"
        )
    )
}
