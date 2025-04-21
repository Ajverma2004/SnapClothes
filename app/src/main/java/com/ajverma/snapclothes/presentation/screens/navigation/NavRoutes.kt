package com.ajverma.snapclothes.presentation.screens.navigation

import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import kotlinx.serialization.Serializable

interface NavRoutes

@Serializable
object Welcome : NavRoutes

@Serializable
object Favourites : NavRoutes

@Serializable
object AuthOption : NavRoutes

@Serializable
data class ProductList(
    val category: String? = null,
    val query: String? = null
) : NavRoutes

@Serializable
data class ProductDetails(
    val productId: String
) : NavRoutes

@Serializable
object ChatBot : NavRoutes

@Serializable
object Carousal : NavRoutes

@Serializable
object Login : NavRoutes

@Serializable
object SignUp : NavRoutes

@Serializable
object Home : NavRoutes