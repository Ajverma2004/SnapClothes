package com.ajverma.snapclothes.presentation.utils.auth_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlreadyHaveAnAccountText(
    modifier: Modifier = Modifier,
    initialText: String = "Already have an account? ",
    initialTextColor: Color = Color.Black,
    trailingText: String,
    trailingTextColor: Color = Color.Black,
    textDecoration: TextDecoration? = null,
    onClick: () -> Unit,
) {
    Text(
        text = buildAnnotatedString {
            append(initialText)
            pushStringAnnotation(tag = trailingText, annotation = trailingText)
            withStyle(style = SpanStyle(
                color = trailingTextColor,
                textDecoration = textDecoration
            )
            ) {
                append(trailingText)
            }
            pop()
        },
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp, color = initialTextColor),
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable {
                onClick()
            }
    )
}