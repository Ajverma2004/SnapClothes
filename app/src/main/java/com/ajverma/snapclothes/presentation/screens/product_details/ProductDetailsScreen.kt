package com.ajverma.snapclothes.presentation.screens.product_details

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.ajverma.snapclothes.data.network.models.BannerResponseItem
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    productId: String,
) {
    LazyColumn {

        item {
            ImageViewer(imagesList = product.image_urls!!)
        }

    }
}


@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    imagesList: List<String>,
) {


    Column(
        modifier = modifier
    ) {

        val pagerState = rememberPagerState(0) {
            imagesList.size
        }


        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) {
            AsyncImage(
                model = imagesList[it],
                contentDescription = "product images",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(7.dp))

        DotsIndicator(
            modifier = Modifier,
            dotCount = imagesList.size,
            pagerState = pagerState,
            type = ShiftIndicatorType(
                DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp,
                    borderWidth = 1.dp,
                    borderColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        )
    }
}