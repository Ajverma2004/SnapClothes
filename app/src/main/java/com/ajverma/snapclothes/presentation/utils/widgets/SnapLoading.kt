package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp


@Composable
fun SnapLoading(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

    val shimmerColors = listOf(
        Color(0xFF30C236),                // Dark base
        Color(0xFFEEEEEE),                // Bright center shimmer
        Color(0xFF359038)                 // Dark base again
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(5.dp)
            .background(Color(0xFF42FF00)) // even darker background
    ) {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(-600f + 1200f * animatedOffset, 0f),
            end = Offset(0f + 1200f * animatedOffset, 0f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        )
    }
}





@Preview
@Composable
private fun SnapLoadingScreenPreview() {
    SnapClothesTheme {
        SnapLoading()
    }
}