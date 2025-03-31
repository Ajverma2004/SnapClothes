package com.ajverma.snapclothes.presentation.utils.widgets

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ajverma.snapclothes.data.network.models.BannerResponse
import com.ajverma.snapclothes.data.network.models.BannerResponseItem
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.firestore.firestore
import com.google.firebase.options
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun SnapBanner(
    modifier: Modifier = Modifier,
    bannerList: List<BannerResponseItem>,
    onBannerClick: (String) -> Unit = {},
) {


    Column(
        modifier = modifier
    ) {

        val pagerState = rememberPagerState(0) {
            bannerList.size
        }

        LaunchedEffect(pagerState.pageCount) {
            while (true) {
                kotlinx.coroutines.delay(5000)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(
                    nextPage,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) {
            AsyncImage(
                model = bannerList[it].banner_url,
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onBannerClick(bannerList[it].category)
                    }
            )
        }

        Spacer(Modifier.height(7.dp))

        DotsIndicator(
            modifier = Modifier,
            dotCount = bannerList.size,
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