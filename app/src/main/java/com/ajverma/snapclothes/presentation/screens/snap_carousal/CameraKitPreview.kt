package com.ajverma.snapclothes.presentation.screens.snap_carousal

import android.view.LayoutInflater
import android.view.ViewStub
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ajverma.snapclothes.BuildConfig
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.R.*
import com.ajverma.snapclothes.data.network.models.LensData
import com.snap.camerakit.Session
import com.snap.camerakit.invoke
import com.snap.camerakit.lenses.LensesComponent
import com.snap.camerakit.lenses.whenHasFirst
import com.snap.camerakit.support.camerax.CameraXImageProcessorSource

@Composable
fun CameraKitPreview(selectedLensId: String?, isFrontCamera: Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lensGroupId = BuildConfig.LENS_GROUP_ID

    val imageProcessorSource = remember {
        CameraXImageProcessorSource(context = context, lifecycleOwner = lifecycleOwner)
    }

    val sessionRef = remember { mutableStateOf<Session?>(null) }

    LaunchedEffect(isFrontCamera) {
        imageProcessorSource.startPreview(isFrontCamera)
    }

    LaunchedEffect(selectedLensId) {
        val session = sessionRef.value
        if (selectedLensId != null && session != null) {
            session.lenses.repository.observe(
                LensesComponent.Repository.QueryCriteria.ById(selectedLensId, lensGroupId)
            ) { result ->
                result.whenHasFirst { lens ->
                    session.lenses.processor.apply(lens)
                    Toast.makeText(context, "Applied: ${lens.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AndroidView(
        factory = { ctx ->
            LayoutInflater.from(ctx).inflate(R.layout.camera_layout, null).apply {
                val viewStub = findViewById<ViewStub>(R.id.camera_kit_stub)
                val session = Session(context = ctx) {
                    imageProcessorSource(imageProcessorSource)
                    attachTo(viewStub)
                }
                sessionRef.value = session
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}



