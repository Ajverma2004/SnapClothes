package com.ajverma.snapclothes

import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.snap.camerakit.Session
import com.snap.camerakit.invoke
import com.snap.camerakit.lenses.LensesComponent
import com.snap.camerakit.lenses.whenHasFirst
import com.snap.camerakit.support.camerax.CameraXImageProcessorSource
import com.snap.camerakit.supported

class CameraActivity : ComponentActivity() {

    private var lensId: String? = null
    private var cameraKitSession: Session? = null
    private lateinit var imageProcessorSource: CameraXImageProcessorSource

    companion object {
        const val LENS_GROUP_ID = BuildConfig.LENS_GROUP_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lensId = intent.getStringExtra("LENS_ID")

        if (lensId == null) {
            Toast.makeText(this, "Lens ID not provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Check if Camera Kit is supported on this device
        if (!supported(this)) {
            Toast.makeText(this, getString(R.string.camera_kit_not_supported), Toast.LENGTH_LONG)
                .show()
            finish()
            return
        }

        // Initialize the image processor source
        imageProcessorSource = CameraXImageProcessorSource(context = this, lifecycleOwner = this)

        setContent {
            SnapClothesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraKitScreen(
                        onCameraPermissionDenied = {
                            Toast.makeText(
                                this,
                                getString(R.string.camera_permission_not_granted),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun CameraKitScreen(onCameraPermissionDenied: () -> Unit) {
        val context = this
        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )
        var isFrontCamera by remember { mutableStateOf(false) }

        if (!cameraPermissionState.status.isGranted) {
            if (cameraPermissionState.status.shouldShowRationale) {
                onCameraPermissionDenied()
            } else {
                LaunchedEffect(Unit) {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        } else {
            imageProcessorSource.startPreview(true)

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "AR Try-On",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isFrontCamera = !isFrontCamera
                                imageProcessorSource.startPreview(
                                    isFrontCamera
                                )
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.switch_camera),
                                    contentDescription = "Switch Camera",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(end = 16.dp).size(40.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AndroidView(
                        factory = { ctx ->
                            LayoutInflater.from(ctx).inflate(R.layout.camera_layout, null).apply {
                                val viewStub = findViewById<ViewStub>(R.id.camera_kit_stub)

                                lensId?.let {
                                    cameraKitSession = Session(context = ctx) {
                                        imageProcessorSource(imageProcessorSource)
                                        attachTo(viewStub)
                                    }.apply {
                                        lenses.repository.observe(
                                            LensesComponent.Repository.QueryCriteria.ById(
                                                lensId!!,
                                                LENS_GROUP_ID
                                            )
                                        ) { result ->
                                            result.whenHasFirst { requestedLens ->
                                                lenses.processor.apply(requestedLens)
                                                Toast.makeText(
                                                    ctx,
                                                    "Applied lens: ${requestedLens.name}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                } ?: run {
                                    (ctx as? ComponentActivity)?.finish()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        // Clean up Camera Kit session
        cameraKitSession?.close()
        super.onDestroy()
    }
}