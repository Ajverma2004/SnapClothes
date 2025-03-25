package com.ajverma.snapclothes.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ajverma.snapclothes.presentation.utils.widgets.ProductsView
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
    val productsList = viewModel.products.collectAsStateWithLifecycle()



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

        LazyColumn(modifier = modifier.fillMaxSize()) {

            item {
                SnapHeader(
                    title = "SnapClothes",
                    showFavourites = false,
                    showBackButton = false
                )
            }

            when (val uiState = state.value) {
                is HomeViewModel.HomeState.Loading -> {
                    item { SnapLoading() }
                }

                is HomeViewModel.HomeState.Error -> {
                    item { SnapError(
                        error = uiState.message,
                        onRetry = {
                            viewModel.getBanners()
                            viewModel.getCategories()
                            viewModel.getProducts()
                        }
                    ) }
                }

                is HomeViewModel.HomeState.Success -> {

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item {
                        SnapBanner(
                            bannerList = bannersList.value
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

//                Text(
//                    "Categories",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                        .align(Alignment.Start),
//                    fontWeight = FontWeight.Bold,
//                )
                    item {
                        SnapOutlinedText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                                ,
                            text = "Categories",
                            color = SnapYellow,
                            strokeColor = Color.Black,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }

                    item {
                        SnapCategories(
                            categories = categoriesList.value
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item {
                        SnapOutlinedText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                            ,
                            text = "Recommendations",
                            color = SnapYellow,
                            strokeColor = Color.Black,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }

                    ProductsView(
                        products = productsList.value,
                        onProductClick = {
                        }
                    )
                }
            }
        }
    }


