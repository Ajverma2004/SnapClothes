package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TypingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Float = 8f,
    circleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Float = 4f,
    travelDistance: Float = 12f
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        circles.forEachIndexed { index, animatable ->
            val scale by animatable.asState()
            Box(
                modifier = Modifier
                    .size(circleSize.dp)
                    .offset(y = -(travelDistance * scale).dp)
                    .background(
                        color = circleColor.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            )
        }
    }
}