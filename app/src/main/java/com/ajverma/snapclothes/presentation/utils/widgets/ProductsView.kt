package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ajverma.jetfoodapp.presentation.utils.widgets.gridItems
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.ui.theme.SnapYellow
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType



fun LazyListScope.ProductsView(
    modifier: Modifier = Modifier,
    products: List<ProductResponseItem>,
    onProductClick: (String) -> Unit
) {
    if (products.isNotEmpty()) {
        gridItems(products,2){ product ->
            ProductItem(
                product = product,
                onProductClick = {
                    onProductClick(product._id)
                }
            )
        }
    }
}

@Composable
fun ProductItem(
    product: ProductResponseItem,
    onProductClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            )
            .background(Color.White)
            .clickable { onProductClick(product._id) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Image Carousel
            ImagePager(
                images = product.image_urls
            )

            // Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "£${product.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )

                    product.rating.let { rating ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = rating.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }

        // ❤️ Favorite Icon - top right of the whole card
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(20.dp)
                .clickable {
                    // TODO: Handle favorite toggle
                }
        )
    }
}





@Composable
fun ImagePager(
    modifier: Modifier = Modifier,
    images: List<String>,
) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState(0) {
            images.size
        }

        LaunchedEffect(pagerState.pageCount) {
            while (true) {
                kotlinx.coroutines.delay(10000)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 1000))
            }
        }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = "product image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Fit
                )

                // Dots Indicator at bottom
                DotsIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 3.dp),
                    dotCount = images.size,
                    pagerState = pagerState,
                    type = ShiftIndicatorType(
                        DotGraphic(
                            color = Color.Gray,
                            size = 3.dp,
                        )
                    )
                )
            }
        }
    }
}
