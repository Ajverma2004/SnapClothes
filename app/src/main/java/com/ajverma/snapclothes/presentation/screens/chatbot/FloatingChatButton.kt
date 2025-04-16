package com.ajverma.snapclothes.presentation.screens.chatbot

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun FloatingChatButton(
    onChatButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    val animatedOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Move every 3 seconds
            scope.launch {
                animatedOffset.animateTo(
                    targetValue = 5f, // Move down by 5 dp
                    animationSpec = tween(durationMillis = 500)
                )
                animatedOffset.animateTo(
                    targetValue = -5f, // Move up by 10 dp
                    animationSpec = tween(durationMillis = 500)
                )
                animatedOffset.animateTo(
                    targetValue = 0f, // Return to original position
                    animationSpec = tween(durationMillis = 500)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .size(56.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .clickable(interactionSource = interactionSource, indication = null) { // No ripple
                onChatButtonClicked()
            }
            .padding(16.dp)
            .offset(y = animatedOffset.value.dp), // Apply vertical offset
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Open Chatbot",
            tint = Color.White
        )
    }
}