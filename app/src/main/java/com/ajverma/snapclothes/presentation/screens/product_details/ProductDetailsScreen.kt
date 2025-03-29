package com.ajverma.snapclothes.presentation.screens.product_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.data.network.models.BannerResponseItem
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.presentation.utils.widgets.NameToColor
import com.ajverma.snapclothes.presentation.utils.widgets.SizesView
import com.ajverma.snapclothes.presentation.utils.widgets.SnapError
import com.ajverma.snapclothes.presentation.utils.widgets.SnapHeader
import com.ajverma.snapclothes.presentation.utils.widgets.SnapLoading
import com.ajverma.snapclothes.ui.theme.SnapYellow
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.flow.collectLatest
import java.util.jar.Attributes.Name

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProductDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    productId: String,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductDetails(productId)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest {
            when (it) {
                ProductDetailsViewModel.ProductDetailsEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    val listState = rememberLazyListState()



    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White),
                state = listState
            ) {


                when (val stateValue = state.value) {
                    is ProductDetailsViewModel.ProductDetailsState.Error -> {
                        item {
                            SnapError(
                                error = stateValue.message,
                                onRetry = {
                                    viewModel.getProductDetails(productId)
                                }
                            )
                        }
                    }

                    ProductDetailsViewModel.ProductDetailsState.Loading -> {
                        item {
                            SnapLoading()
                        }
                    }

                    is ProductDetailsViewModel.ProductDetailsState.Success -> {

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Brand: ${stateValue.data.brand}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier.padding(end = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stateValue.data.rating.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }

                            Text(
                                text = stateValue.data.name,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(bottom = 6.dp, start = 16.dp, end = 16.dp),
                                maxLines = 2
                            )
                        }


                        item {
                            ImageViewer(
                                imagesList = stateValue.data.image_urls,
                                productId = productId
                            )
                        }

                        item {
                            NameToColor(
                                colorList = stateValue.data.colors_available
                            )
                        }

                        item {
                            SizesView(
                                sizeList = stateValue.data.sizes_available
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        item {
                            PriceAndExpandableDescription(
                                price = stateValue.data.price,
                                description = stateValue.data.description
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }


                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TryARButton()
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                BuyNowButton()
                            }
                        }




                    }
                }

            }
        }


        val isTryButtonVisible by remember {
            derivedStateOf {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                val secondLastIndex = listState.layoutInfo.totalItemsCount - 2
                visibleItems.any { it.index == secondLastIndex }
            }
        }
        val isBuyButtonVisible by remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
            }
        }


        if (!isTryButtonVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 16.dp, bottom = 110.dp, end = 16.dp, top = 16.dp)
            ) {
                FloatingTryARButton()
            }
        }
        if (!isBuyButtonVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 16.dp, bottom = 30.dp, end = 16.dp, top = 16.dp)
            ) {
                FloatingBuyNowButton()
            }
        }
    }
}


@Composable
fun BuyNowButton() {
    Button(
        onClick = { /* Handle buy action */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp, end = 7.dp, bottom = 10.dp)
            .height(50.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(8.dp),
            )
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text("Buy Now")
    }
}

@Composable
fun TryARButton() {
    Button(
        onClick = { /* Handle AR try-on */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp)
            .height(50.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(8.dp),
            )
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp) // Remove default padding to align image
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.space),
                contentDescription = "Space Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Centered Text
                Text(
                    text = "Try On",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.clothing),
                    contentDescription = "AR Icon",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Composable
fun FloatingBuyNowButton() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Black, shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { /* Handle AR Try-On */ }
    ) {

        // Foreground AR icon
        Image(
            painter = painterResource(id = R.drawable.buy),
            contentDescription = "Try AR",
            modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
    }
}

@Composable
fun FloatingTryARButton() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable { /* Handle AR Try-On */ }
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.space),
            contentDescription = "AR Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Foreground AR icon
        Image(
            painter = painterResource(id = R.drawable.clothing), // Your AR icon here
            contentDescription = "Try AR",
            modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}



@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    imagesList: List<String>,
    productId: String,
) {
    val pagerState = rememberPagerState(0) {
        imagesList.size
    }

    Column(
        modifier = modifier
            .background(Color(0x8FF1F1F1))
    ) {


        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) {
            AsyncImage(
                model = imagesList[it],
                contentDescription = "product images",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(570.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }

    Spacer(Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 10.dp)
    ) {
        // Centered DotsIndicator
        DotsIndicator(
            dotCount = imagesList.size,
            pagerState = pagerState,
            type = ShiftIndicatorType(
                DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp,
                    borderWidth = 1.dp,
                    borderColor = MaterialTheme.colorScheme.onPrimary
                )
            ),
            modifier = Modifier.align(Alignment.Center)
        )
        FavoriteIconWithToggle()
    }


    HorizontalDivider(Modifier.fillMaxWidth())

}


@Composable
fun BoxScope.FavoriteIconWithToggle() {
    var isFavorite by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Icon(
        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = "Favorite",
        tint = if (isFavorite) Color.Black else MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                isFavorite = !isFavorite
            }
    )
}

@Composable
fun PriceAndExpandableDescription(
    price: Double,
    description: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 💷 Price
        Text(
            text = "£$price",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description (initial preview)
        if (!expanded) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 8,
                color = Color.DarkGray
            )
        }

        // Animated expandable text
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }

        // See more / less toggle
        Text(
            text = if (expanded) "See less" else "See more",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { expanded = !expanded }
        )
    }
}
