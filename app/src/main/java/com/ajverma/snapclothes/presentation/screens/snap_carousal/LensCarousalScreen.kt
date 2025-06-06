package com.ajverma.snapclothes.presentation.screens.snap_carousal

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.data.network.models.LensData
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import kotlin.math.abs

val LENS_ITEM_SIZE = 70.dp
val LENS_ITEM_SPACING = 16.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun LensCarouselScreen(
    viewModel: LensViewModel = hiltViewModel(),
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    val products = homeViewModel.products.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        } else {
            hasPermission = true
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        hasPermission = cameraPermissionState.status.isGranted
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Camera permission is required to try lenses.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3590F3))
                ) {
                    Text("Open Settings", color = Color.White)
                }
            }
        }
        return
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - LENS_ITEM_SIZE) / 2

    var isFrontCamera by remember { mutableStateOf(false) }
    var isCameraReady by remember { mutableStateOf(false) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val itemWidthPx = with(density) { (LENS_ITEM_SIZE + LENS_ITEM_SPACING).toPx() }
                val centerItemIndex = if (offset > itemWidthPx / 2) index + 1 else index

                if (centerItemIndex != uiState.selectedLensIndex &&
                    centerItemIndex in uiState.lenses.indices
                ) {
                    viewModel.onLensTapped(centerItemIndex)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        CameraKitPreview(
            selectedLensId = uiState.currentlyAppliedLensId,
            isFrontCamera = isFrontCamera,
            onCameraReady = { isCameraReady = true }
        )

        if (!isCameraReady) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        IconButton(
            onClick = { isFrontCamera = !isFrontCamera },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.switch_camera),
                contentDescription = "Switch Camera",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        ) {

            val currentLens = uiState.lenses.getOrNull(uiState.selectedLensIndex)
            val matchedProduct = products.value.find { it.lensID == currentLens?.id }

            Text(
                text = "BUY",
                color = Color.White,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ){
                        if (matchedProduct != null) {
                            navController.navigate(ProductDetails(matchedProduct._id))
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )

            // Carousel
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(LENS_ITEM_SIZE)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyRow(
                    state = lazyListState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LENS_ITEM_SPACING),
                    contentPadding = PaddingValues(horizontal = contentPadding),
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
                ) {
                    itemsIndexed(uiState.lenses, key = { _, lens -> lens.id }) { index, lens ->
                        val isSelected = index == uiState.selectedLensIndex

                        LensItem(
                            lens = lens,
                            isSelected = isSelected,
                            itemSize = LENS_ITEM_SIZE,
                            haptic = haptic,
                            onLensClick = {
                                viewModel.onLensTapped(index)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(index)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun LensItem(
    lens: LensData,
    isSelected: Boolean,
    itemSize: Dp,
    haptic: HapticFeedback,
    onLensClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = spring(), label = "scale"
    )
    val borderSize by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 0.dp,
        animationSpec = spring(), label = "borderSize"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = spring(), label = "borderColor"
    )
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .size(itemSize)
            .scale(scale)
            .clip(CircleShape)
            .border(borderSize, borderColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onLensClick()
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(lens.iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = lens.name,
            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
            error = painterResource(id = android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
        )


        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.0f),
                                Color.Black.copy(alpha = 0.3f)
                            ),
                            radius = with(density) { (itemSize / 2).toPx() }
                        )
                    )
            )
        }
    }
}