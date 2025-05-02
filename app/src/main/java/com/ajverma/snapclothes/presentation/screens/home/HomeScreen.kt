package com.ajverma.snapclothes.presentation.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ajverma.snapclothes.presentation.screens.auth.AuthBaseViewModel
import com.ajverma.snapclothes.presentation.screens.navigation.Carousal
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.ajverma.snapclothes.presentation.screens.navigation.ProductList
import com.ajverma.snapclothes.presentation.screens.navigation.Welcome
import com.ajverma.snapclothes.presentation.screens.products_list.ProductsListViewModel
import com.ajverma.snapclothes.presentation.utils.widgets.ARHomeHeader
import com.ajverma.snapclothes.presentation.utils.widgets.ProductsView
import com.ajverma.snapclothes.presentation.utils.widgets.SnapCategories
import com.ajverma.snapclothes.presentation.utils.widgets.SnapError
import com.ajverma.snapclothes.presentation.utils.widgets.SnapHeader
import com.ajverma.snapclothes.presentation.utils.widgets.SnapLoading
import com.ajverma.snapclothes.ui.theme.SnapYellow
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val categoriesList = viewModel.categories.collectAsStateWithLifecycle()
    val productsList = viewModel.products.collectAsStateWithLifecycle()



    LaunchedEffect(true) {
        viewModel.event.collectLatest {
            when (it) {
                is HomeViewModel.HomeEvent.NavigateToDetails -> {
                    navController.navigate(ProductDetails(it.productId))
                }

                is HomeViewModel.HomeEvent.NavigateToProductsScreen -> {
                    navController.navigate(productListRoute(category = it.category))
                }

                is HomeViewModel.HomeEvent.NavigateToAllProductsScreen -> {
                    navController.navigate(productListRoute())
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {


            when (val uiState = state.value) {
                is HomeViewModel.HomeState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            SnapLoading()
                        }
                    }
                }

                is HomeViewModel.HomeState.Error -> {
                    item {
                        SnapError(
                            error = uiState.message,
                            onRetry = {
                                viewModel.getCategories()
                                viewModel.getProducts()
                            }
                        )
                    }
                }

                is HomeViewModel.HomeState.Success -> {

                    item {
                        ARHomeHeader(
                            onLivePreviewClick = {
                            },
                            onCameraTryOnClick = {
                                navController.navigate(Carousal)
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }


                    item {
                        Text(
                            "Categories",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }

                    item {
                        SnapCategories(
                            categories = categoriesList.value,
                            onClick = {
                                viewModel.onCategoryClicked(it)
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "More products",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(10.dp),
                                fontWeight = FontWeight.Bold,
                            )
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        viewModel.onSeeAllClicked()
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "See all",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Bold,
                                )

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color.Black,
                                )

                            }

                        }
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }


                    ProductsView(
                        products = productsList.value,
                        onProductClick = {
                            viewModel.onProductClicked(it)
                        },
                    )
                }
            }
        }
    }
}

fun productListRoute(
    category: String? = null,
    query: String? = null
): String {
    val base = "ProductList"
    val params = mutableListOf<String>()
    category?.let { params.add("category=$it") }
    query?.let { params.add("query=$it") }

    return if (params.isNotEmpty()) {
        "$base?${params.joinToString("&")}"
    } else {
        base
    }
}



