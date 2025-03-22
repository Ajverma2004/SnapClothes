package com.ajverma.snapclothes.presentation.screens.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ajverma.snapclothes.presentation.screens.welcome.WelcomeScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SnapNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    SharedTransitionScope {
        NavHost(navController = navController, startDestination = Welcome){
            composable<Welcome>{
                WelcomeScreen(navController = navController)
            }

            composable<AuthOption>{
                AuthOptionScreen(navController = navController)
            }
        }
    }
}