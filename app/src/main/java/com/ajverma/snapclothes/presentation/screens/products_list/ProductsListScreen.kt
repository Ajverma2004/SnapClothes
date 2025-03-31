package com.ajverma.snapclothes.presentation.screens.products_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ajverma.snapclothes.domain.utils.toTitleCase
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.ajverma.snapclothes.presentation.utils.mapper.nameFromCategory
import com.ajverma.snapclothes.presentation.utils.widgets.ProductsView
import com.ajverma.snapclothes.presentation.utils.widgets.SnapError
import com.ajverma.snapclothes.presentation.utils.widgets.SnapHeader
import com.ajverma.snapclothes.presentation.utils.widgets.SnapLoading
import com.ajverma.snapclothes.ui.theme.SnapYellow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun ProductsListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductsListViewModel = hiltViewModel(),
    navController: NavController,
    category: String? = null,
    query: String? = null,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(category, query) {
        when {
            query != null -> viewModel.search(query)
            category != null -> viewModel.getProductsByCategory(category)
            else -> viewModel.getProducts()
        }
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ProductsListViewModel.ProductsListEvent.OnProductClicked -> {
                    navController.navigate(
                        ProductDetails(
                            productId = event.productId
                        )
                    )
                }

                is ProductsListViewModel.ProductsListEvent.OnBackClicked -> {
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        when (val stateValue = state.value) {
            is ProductsListViewModel.ProductsListState.Loading -> {
                item {
                    SnapLoading()
                }
            }

            is ProductsListViewModel.ProductsListState.Error -> {
                item {
                    SnapError(
                        error = stateValue.message,
                        onRetry = {
                            when {
                                query != null -> viewModel.search(query)
                                category != null -> viewModel.getProductsByCategory(category)
                                else -> viewModel.getProducts()
                            }
                        }
                    )
                }
            }

            is ProductsListViewModel.ProductsListState.Success -> {

                item {
                    BackButtonWithCategoryName(
                        category = category,
                        query = query
                    )
                }


                if (stateValue.products.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No items found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    item {
                        Spacer(Modifier.height(10.dp))
                    }

                    ProductsView(
                        products = stateValue.products,
                        onProductClick = {
                            viewModel.onProductClicked(it)
                        },
                    )
                }

            }

        }
    }

}


@Composable
fun BackButtonWithCategoryName(
    modifier: Modifier = Modifier,
    category: String? = null,
    query: String? = null
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFFF5F5F5)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.size(3.dp))

            Text(
                text = if (query != null) "Search for '\'${query}\''"
                    else if(category != null) nameFromCategory(category)
                    else "All Products",
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        HorizontalDivider(Modifier.fillMaxWidth())
    }
}


