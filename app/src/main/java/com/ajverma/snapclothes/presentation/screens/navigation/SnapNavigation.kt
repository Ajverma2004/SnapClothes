package com.ajverma.snapclothes.presentation.screens.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionScreen
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionViewModel
import com.ajverma.snapclothes.presentation.screens.auth.login.LoginScreen
import com.ajverma.snapclothes.presentation.screens.auth.sign_up.SignupScreen
import com.ajverma.snapclothes.presentation.screens.home.HomeScreen
import com.ajverma.snapclothes.presentation.screens.product_details.ProductDetailsScreen
import com.ajverma.snapclothes.presentation.screens.products_list.ProductsListScreen

import com.ajverma.snapclothes.presentation.screens.welcome.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun SnapNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onScreenChanged: (Boolean) -> Unit,
    viewModel: AuthOptionViewModel = hiltViewModel()
) {

    SharedTransitionLayout{
        NavHost(navController = navController, startDestination = if (viewModel.isSignedIn()) Home else Welcome){
            composable<Welcome>{
                WelcomeScreen(navController = navController)
            }

            composable<AuthOption>{
                AuthOptionScreen(navController = navController)
            }

            composable<SignUp> {
                SignupScreen(navController = navController)
            }

            composable<Login> {
                LoginScreen(navController = navController)
            }

            composable<Favourites> {
                onScreenChanged(true)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Favourites", color = Color.Black)
                }
            }

            composable<Home> {
                onScreenChanged(true)
                HomeScreen(
                    navController = navController,
                )
            }

            composable(
                route = "ProductList?category={category}",
                arguments = listOf(
                    navArgument("category") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")

                ProductsListScreen(
                    navController = navController,
                    category = category,
                )
            }

            composable<ProductDetails>{
                onScreenChanged(false)
                val productId = it.arguments?.getString("productId")
                ProductDetailsScreen(
                    navController = navController,
                    productId = productId!!
                )
            }


        }
    }
}