package com.ajverma.snapclothes.presentation.screens.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionScreen
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionViewModel
import com.ajverma.snapclothes.presentation.screens.auth.sign_up.SignupScreen

import com.ajverma.snapclothes.presentation.screens.welcome.WelcomeScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SnapNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AuthOptionViewModel = hiltViewModel()
) {

    SharedTransitionScope {
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

            composable<Home> {
                val navEvent = viewModel.navigationEvent.collectAsStateWithLifecycle(initialValue = null)

                // observe the navigation event
                LaunchedEffect(navEvent.value) {
                    when (navEvent.value) {
                        is AuthOptionViewModel.AuthNavigationEvent.NavigateToWelcomeScreen -> {
                            navController.navigate(Welcome) {
                                popUpTo(Home) {
                                    inclusive = true
                                }
                            }
                        }
                        else -> Unit
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.onSignOutClick()
                        }
                    ) {
                        Text(text = "sign out", color = Color.Black)
                    }
                }
            }

        }
    }
}