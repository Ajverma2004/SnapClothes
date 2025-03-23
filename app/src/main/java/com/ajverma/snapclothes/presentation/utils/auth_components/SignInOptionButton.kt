package com.ajverma.snapclothes.presentation.utils.auth_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInOptionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    elevation: Dp = 0.dp,
    image: Int,
    text: Int,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,

        )
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
        )
        Text(
            stringResource(text),
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}