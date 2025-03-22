package com.ajverma.snapclothes.presentation.screens.navigation

import kotlinx.serialization.Serializable

interface NavRoutes

@Serializable
object Welcome : NavRoutes

@Serializable
object AuthOption : NavRoutes

@Serializable
object Login : NavRoutes

@Serializable
object SignUp : NavRoutes

@Serializable
object Home : NavRoutes