package com.ajverma.snapclothes.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ajverma.snapclothes.presentation.utils.widgets.SnapBanner
import com.ajverma.snapclothes.presentation.utils.widgets.SnapCategories
import com.ajverma.snapclothes.presentation.utils.widgets.SnapError
import com.ajverma.snapclothes.presentation.utils.widgets.SnapHeader
import com.ajverma.snapclothes.presentation.utils.widgets.SnapLoading
import com.ajverma.snapclothes.presentation.utils.widgets.SnapOutlinedText
import com.ajverma.snapclothes.ui.theme.SnapYellow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val bannersList = viewModel.banners.collectAsStateWithLifecycle()
    val categoriesList = viewModel.categories.collectAsStateWithLifecycle()



    LaunchedEffect(true) {
        viewModel.event.collectLatest {
            when (it) {
                is HomeViewModel.HomeEvent.NavigateToDetails -> {
//                    navController.navigate("details/${it.id}")
                }

                else -> {}
            }
        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        SnapHeader(
            title = "SnapClothes",
            showFavourites = true,
            showBackButton = true
        )

        when (val uiState = state.value) {
            is HomeViewModel.HomeState.Loading -> {
                SnapLoading()
            }

            is HomeViewModel.HomeState.Error -> {
                SnapError(
                    error = uiState.message,
                    onRetry = {
                        viewModel.getBanners()
                    }
                )
            }

            is HomeViewModel.HomeState.Success -> {

                Spacer(modifier = Modifier.height(20.dp))

                SnapBanner(
                    bannerList = bannersList.value
                )

                Spacer(modifier = Modifier.height(20.dp))

//                Text(
//                    "Categories",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                        .align(Alignment.Start),
//                    fontWeight = FontWeight.Bold,
//                )
                SnapOutlinedText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                        .align(Alignment.Start),
                    text = "Categories",
                    color = SnapYellow,
                    strokeColor = Color.Black,
                    style =  MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))
                Log.d("Categories", categoriesList.value.toString())
                SnapCategories(
                    categories = categoriesList.value
                )




            }
        }


    }


}


