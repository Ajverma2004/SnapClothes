package com.ajverma.snapclothes.presentation.screens.welcome

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.setSingletonImageLoaderFactory
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel
import com.ajverma.snapclothes.presentation.screens.navigation.AuthOption
import com.ajverma.snapclothes.ui.theme.SnapYellow
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

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
        viewModel.getCategories()
        viewModel.getBanners()
    }


    // SceneView setup
    val engine = rememberEngine()
    val view = rememberView(engine)
    val renderer = rememberRenderer(engine)
    val scene = rememberScene(engine)
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val collisionSystem = rememberCollisionSystem(view)


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scene(
            modifier = Modifier
                .fillMaxSize()
                .background(SnapYellow),
            engine = engine,
            view = view,
            renderer = renderer,
            scene = scene,
            modelLoader = modelLoader,
            materialLoader = materialLoader,
            environmentLoader = environmentLoader,
            collisionSystem = collisionSystem,
            isOpaque = true,
            mainLightNode = rememberMainLightNode(engine) {
                intensity = 100_000.0f
            },
            environment = rememberEnvironment(environmentLoader) {
                environmentLoader.createHDREnvironment(
                    assetFileLocation = "environments/yellow_environment.hdr"
                )!!
            },
            cameraNode = rememberCameraNode(engine) {
                position = Position(z = 4.0f)
            },
            childNodes = rememberNodes {
                // 3D Model Node without `centerOrigin`
                add(
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance(
                            assetFileLocation = "models/cartoon_hello.glb"
                        ),
                        scaleToUnits = 1.0f
                    ).apply {
                        transform(
                            position = Position(0.0f, 0.0f, 0.0f)
                        )
                    }
                )
            },
            onTouchEvent = { _: MotionEvent, hitResult: HitResult? ->
                hitResult?.let {
                    println("Tapped at ${it.worldPosition}")
                }
                false
            }
        )

        Button(
            onClick = {
                navController.navigate(AuthOption)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .padding(24.dp), // Optional padding
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White // White background
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
