package com.ajverma.snapclothes.presentation.screens.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ajverma.snapclothes.data.network.Constants.PRODUCT_LIST_BASE
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionScreen
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionViewModel
import com.ajverma.snapclothes.presentation.screens.auth.login.LoginScreen
import com.ajverma.snapclothes.presentation.screens.auth.sign_up.SignupScreen
import com.ajverma.snapclothes.presentation.screens.favourite.FavouriteScreen
import com.ajverma.snapclothes.presentation.screens.home.HomeScreen
import com.ajverma.snapclothes.presentation.screens.product_details.ProductDetailsScreen
import com.ajverma.snapclothes.presentation.screens.products_list.ProductsListScreen
import com.ajverma.snapclothes.presentation.screens.welcome.WelcomeScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import com.ajverma.snapclothes.presentation.screens.chatbot.ChatBotScreen


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun SnapNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onScreenChanged: (Boolean) -> Unit,
    viewModel: AuthOptionViewModel = hiltViewModel(),
    onBackWhileSearchFocused: () -> Boolean,
) {

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = if (viewModel.isSignedIn()) Home else Welcome,
            enterTransition = {
                getCustomEnterTransition(
                    initialState.destination.route,
                    targetState.destination.route
                )
            },
            exitTransition = {
                getCustomExitTransition(
                    initialState.destination.route,
                    targetState.destination.route
                )
            },
            popEnterTransition = {
                getCustomEnterTransition(
                    initialState.destination.route,
                    targetState.destination.route
                )
            },
            popExitTransition = {
                getCustomExitTransition(
                    initialState.destination.route,
                    targetState.destination.route
                )
            }
        ) {
            composable<Welcome> {
                onScreenChanged(false)
                WelcomeScreen(navController = navController)
            }

            composable<AuthOption> {
                onScreenChanged(false)
                AuthOptionScreen(navController = navController)
            }

            composable<SignUp> {
                onScreenChanged(false)
                SignupScreen(navController = navController)
            }

            composable<Login> {
                onScreenChanged(false)
                LoginScreen(navController = navController)
            }

            composable<Favourites> {
                onScreenChanged(true)
                FavouriteScreen(
                    navController = navController
                )
            }

            composable<Home> {
                onScreenChanged(true)
                HomeScreen(
                    navController = navController,
                )
            }

            composable(
                route = "$PRODUCT_LIST_BASE?category={category}&query={query}",
                arguments = listOf(
                    navArgument("category") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("query") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                val query = backStackEntry.arguments?.getString("query")
                onScreenChanged(false)
                ProductsListScreen(
                    navController = navController,
                    category = category,
                    query = query
                )
            }


            composable<ProductDetails> {
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




fun getCustomEnterTransition(from: String?, to: String?): EnterTransition {
    return when {
        // Home <-> Favourites
        from == Home::class.qualifiedName && to == Favourites::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == Favourites::class.qualifiedName && to == Home::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // Home <-> ChatBot
        from == Home::class.qualifiedName && to == ChatBot::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == ChatBot::class.qualifiedName && to == Home::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // Favourites <-> ChatBot
        from == Favourites::class.qualifiedName && to == ChatBot::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == ChatBot::class.qualifiedName && to == Favourites::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // Home <-> ProductDetails
        from == Home::class.qualifiedName && to == ProductDetails::class.qualifiedName -> slideInVertically { it } + fadeIn()
        from == ProductDetails::class.qualifiedName && to == Home::class.qualifiedName -> slideInVertically { -it } + fadeIn()

        // Home <-> ProductsList
        from == Home::class.qualifiedName && to == ProductList::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == ProductList::class.qualifiedName && to == Home::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // ProductList <-> ProductDetails
        from == ProductList::class.qualifiedName && to == ProductDetails::class.qualifiedName -> slideInVertically { it } + fadeIn()
        from == ProductDetails::class.qualifiedName && to == ProductList::class.qualifiedName -> slideInVertically { -it } + fadeIn()

        // Welcome <-> AuthOption
        from == Welcome::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == AuthOption::class.qualifiedName && to == Welcome::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // AuthOption <-> Login
        from == AuthOption::class.qualifiedName && to == Login::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == Login::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // AuthOption <-> SignUp
        from == AuthOption::class.qualifiedName && to == SignUp::class.qualifiedName -> slideInHorizontally { it } + fadeIn()
        from == SignUp::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideInHorizontally { -it } + fadeIn()

        // SignUp <-> Login (Fade)
        (from == SignUp::class.qualifiedName && to == Login::class.qualifiedName) ||
                (from == Login::class.qualifiedName && to == SignUp::class.qualifiedName) -> fadeIn(animationSpec = tween(300))

        // Auth screens to Home
        (from == Login::class.qualifiedName || from == SignUp::class.qualifiedName || from == AuthOption::class.qualifiedName) && to == Home::class.qualifiedName -> slideInHorizontally { it } + fadeIn()

        else -> fadeIn()
    }
}

fun getCustomExitTransition(from: String?, to: String?): ExitTransition {
    return when {
        // Home <-> Favourites
        from == Home::class.qualifiedName && to == Favourites::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == Favourites::class.qualifiedName && to == Home::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // Home <-> ChatBot
        from == Home::class.qualifiedName && to == ChatBot::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == ChatBot::class.qualifiedName && to == Home::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // Favourites <-> ChatBot
        from == Favourites::class.qualifiedName && to == ChatBot::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == ChatBot::class.qualifiedName && to == Favourites::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()


        // Home <-> ProductDetails
        from == Home::class.qualifiedName && to == ProductDetails::class.qualifiedName -> slideOutVertically { -it } + fadeOut()
        from == ProductDetails::class.qualifiedName && to == Home::class.qualifiedName -> slideOutVertically { it } + fadeOut()

        // Home <-> ProductsList
        from == Home::class.qualifiedName && to == ProductList::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == ProductList::class.qualifiedName && to == Home::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // ProductList <-> ProductDetails
        from == ProductList::class.qualifiedName && to == ProductDetails::class.qualifiedName -> slideOutVertically { -it } + fadeOut()
        from == ProductDetails::class.qualifiedName && to == ProductList::class.qualifiedName -> slideOutVertically { it } + fadeOut()

        // Welcome <-> AuthOption
        from == Welcome::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == AuthOption::class.qualifiedName && to == Welcome::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // AuthOption <-> Login
        from == AuthOption::class.qualifiedName && to == Login::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == Login::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // AuthOption <-> SignUp
        from == AuthOption::class.qualifiedName && to == SignUp::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()
        from == SignUp::class.qualifiedName && to == AuthOption::class.qualifiedName -> slideOutHorizontally { it } + fadeOut()

        // SignUp <-> Login (Fade)
        (from == SignUp::class.qualifiedName && to == Login::class.qualifiedName) ||
                (from == Login::class.qualifiedName && to == SignUp::class.qualifiedName) -> fadeOut(animationSpec = tween(300))

        // Auth screens to Home
        (from == Login::class.qualifiedName || from == SignUp::class.qualifiedName || from == AuthOption::class.qualifiedName) && to == Home::class.qualifiedName -> slideOutHorizontally { -it } + fadeOut()

        else -> fadeOut()
    }
}


