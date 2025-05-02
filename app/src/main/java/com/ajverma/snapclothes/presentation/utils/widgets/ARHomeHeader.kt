package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import com.ajverma.snapclothes.ui.theme.SnapYellow
import kotlinx.coroutines.delay

val CurvedBottomShape = object : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): androidx.compose.ui.graphics.Outline {
        val path = Path().apply {
            lineTo(0f, 0f)
            lineTo(0f, size.height - with(density) { 40.dp.toPx() })
            quadraticTo(
                size.width / 2f, size.height + with(density) { 20.dp.toPx() },
                size.width, size.height - with(density) { 40.dp.toPx() }
            )
            lineTo(size.width, 0f)
            close()
        }
        return androidx.compose.ui.graphics.Outline.Generic(path)
    }
}

@Composable
fun ARHomeHeader(
    modifier: Modifier = Modifier,
    onLivePreviewClick: () -> Unit = {},
    onCameraTryOnClick: () -> Unit = {}
) {
    var livePreviewClicked by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "Color Animation")

    var isDark by remember { mutableStateOf(false) }
    val buttonContainerColor = if (isDark) Color.Black else Color.White
    val buttonTextColor = if (isDark) Color.White else Color.Black

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            isDark = !isDark
        }
    }

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "X sway"
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Y sway"
    )

    // Image change state with slower animation
    var currentImage by remember { mutableStateOf(R.drawable.tshirt_on_hanger) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500) // Change image every 1.5 seconds
            currentImage = if (currentImage == R.drawable.tshirt_on_hanger) {
                R.drawable.tshirt_on_person
            } else {
                R.drawable.tshirt_on_hanger
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(SnapYellow, shape = CurvedBottomShape)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (title, livePreviewButton, cameraTryOnButton, mockupImage) = createRefs()

            // Title
            Text(
                text = "TRY CLOTHES IN\nAR NOW",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 40.sp,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            // LIVE PREVIEW button
            Button(
                onClick = onLivePreviewClick,
                modifier = Modifier
                    .width(180.dp)
                    .constrainAs(livePreviewButton) {
                        top.linkTo(title.bottom, margin = 26.dp)
                        start.linkTo(parent.start)
                    }
                    .padding(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonTextColor
                ),
                border = BorderStroke(1.dp, Color.Black),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text("LIVE PREVIEW", fontSize = 16.sp)
            }

            // CAMERA TRY-ON button
            Button(
                onClick = onCameraTryOnClick,
                modifier = Modifier
                    .width(180.dp)
                    .constrainAs(cameraTryOnButton) {
                        top.linkTo(livePreviewButton.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                    .padding(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text("CAMERA TRY-ON", fontSize = 16.sp)
            }


            Image(
                    painter = painterResource(id = currentImage),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(200.dp)
                        .height(300.dp)
                        .constrainAs(mockupImage) {
                            top.linkTo(parent.top, margin = 32.dp)
                            end.linkTo(parent.end, margin = -(26).dp)
                            bottom.linkTo(parent.bottom, margin = -86.dp)
                        }
                        .offset(x = offsetX.dp, y = offsetY.dp)
                )
            }

    }
}

@Preview(showBackground = true)
@Composable
private fun ARHomeHeaderPreview() {
    SnapClothesTheme {
        ARHomeHeader()
    }
}