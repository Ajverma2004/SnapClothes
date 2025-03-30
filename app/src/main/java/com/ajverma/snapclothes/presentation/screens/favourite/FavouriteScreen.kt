package com.ajverma.snapclothes.presentation.screens.favourite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.ajverma.snapclothes.presentation.utils.widgets.ProductsView

@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouriteViewModel = hiltViewModel(),
    navController: NavController
) {

    val favourites by viewModel.favourites.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize()
            .background(Color.White)
    ) {

        if (favourites.isEmpty()) {
            item() {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No favourites 😣",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        } else{
            ProductsView(
                products = favourites.map {
                    ProductResponseItem(
                        _id = it.id,
                        name = it.name,
                        price = it.price,
                        rating = it.rating,
                        image_urls = it.image_urls,
                        arlink = "",
                        brand = "",
                        buyLink = "",
                        category = "",
                        colors_available = emptyList(),
                        description = "",
                        sizes_available = ArrayList(emptyList())
                    )
                },
                onProductClick = { productId ->
                    navController.navigate(ProductDetails(productId))
                },
                isFavouriteScreen = true
            )
        }

    }



}