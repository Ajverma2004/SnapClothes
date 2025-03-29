package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizesView(
    modifier: Modifier = Modifier,
    sizeList: List<String>,
) {
    val initialSize = remember { sizeList.distinct().firstOrNull() }
    var selectedSize by remember { mutableStateOf(initialSize) }

    Column(modifier = modifier.padding(16.dp)) {

        // Always show selected size, left aligned
        Text(
            text = buildAnnotatedString {
                append("Size: ")
                withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(selectedSize ?: "N/A")
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
            sizeList.distinct().forEach { size ->
                val isSelected = size == selectedSize

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .size(40.dp)
                        .shadow(0.dp, shape = RoundedCornerShape(3.dp))
                        .background(
                            color = if (isSelected) Color.Black else Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(3.dp)
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                        ) {
                            selectedSize = size
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = size,
                        color = if (isSelected) Color.White else Color.Black,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SizesViewPreview() {
    SizesView(
        sizeList = listOf("XS", "S", "M", "L", "XL", "XXL", "3XL")
    )
}
