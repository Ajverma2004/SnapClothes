package com.ajverma.snapclothes.presentation.screens.welcome

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.setSingletonImageLoaderFactory
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel
import com.ajverma.snapclothes.presentation.screens.navigation.AuthOption
import com.ajverma.snapclothes.ui.theme.SnapYellow
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
        viewModel.getCategories()
        viewModel.getBanners()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnapYellow)
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Url("https://lottie.host/c0680a2f-43af-4943-82e7-cd039016b195/kV3RCeFJNx.lottie"),
            autoplay = true,
            loop = true,
            speed = 1f,
            useFrameInterpolation = false,
            playMode = Mode.FORWARD,
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        )

        Button(
            onClick = {
                navController.navigate(AuthOption)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp
            )
        ) {
            Text(
                text = "Welcome to SnapClothes",
                color = Color.Black, // Or SnapYellow for branding
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
