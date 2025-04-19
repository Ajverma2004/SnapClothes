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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ajverma.snapclothes.R
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@Composable
fun FloatingChatButton(
    onChatButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val animatedOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            scope.launch {
                animatedOffset.animateTo(5f, tween(500))
                animatedOffset.animateTo(-5f, tween(500))
                animatedOffset.animateTo(0f, tween(500))
            }
        }
    }
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .padding(16.dp)
            .size(66.dp)
            .offset(y = animatedOffset.value.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape
            )
            .background(color = Color.White, shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onChatButtonClicked()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
              },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ai_chatbot),
            contentDescription = "Chat",
            tint = Color.Black,
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
        )
    }
}

