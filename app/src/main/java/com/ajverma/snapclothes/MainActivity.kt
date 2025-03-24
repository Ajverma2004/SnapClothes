package com.ajverma.snapclothes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
import com.ajverma.snapclothes.presentation.screens.navigation.Home
import com.ajverma.snapclothes.presentation.screens.navigation.NavRoutes
import com.ajverma.snapclothes.presentation.screens.navigation.SnapNavigation
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import com.ajverma.snapclothes.ui.theme.SnapYellow
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showSplashScreen = true

    @Inject
    lateinit var facebookAuthClient: FacebookAuthClient

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnapClothesTheme {

                val context = this

                val navItems = listOf(
                    BottomNavItems.Home,
                    BottomNavItems.Favourites
                )

                var showBottomNav by rememberSaveable { mutableStateOf(false) }
                val navController = rememberNavController()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination
                        AnimatedVisibility(visible = showBottomNav) {
                            Box(
                                modifier = Modifier
                            ) {
                                NavigationBar(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 16.dp,
                                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                            clip = true
                                        ),
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White,
                                    tonalElevation = 0.dp,
                                ) {
                                    navItems.forEach { item ->
                                        val selected =
                                            currentRoute?.hierarchy?.any { it.route == item.route::class.qualifiedName } == true

                                        NavigationBarItem(
                                            selected = selected,
                                            onClick = {
                                                navController.navigate(item.route)
                                            },
                                            icon = {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                ) {
                                                    Icon(
                                                        imageVector = item.icon,
                                                        contentDescription = null,
                                                        tint = if (selected) Color.Black else Color.Gray,
                                                        modifier = Modifier.align(Alignment.Center)
                                                    )
                                                }
                                            },
                                            colors = NavigationBarItemDefaults.colors(
                                                indicatorColor = Color.White,
                                                selectedIconColor = Color.Black,
                                                unselectedIconColor = Color.Gray
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        SnapNavigation(
                            navController = navController,
                            onScreenChanged = {showBottomNav = it},
                        )
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            showSplashScreen = false
        }
    }


    sealed class BottomNavItems(val route: NavRoutes, val icon: ImageVector) {
        data object Home : BottomNavItems(
            com.ajverma.snapclothes.presentation.screens.navigation.Home,
            Icons.Default.Home
        )

        data object Favourites : BottomNavItems(
            com.ajverma.snapclothes.presentation.screens.navigation.Favourites,
            Icons.Filled.Favorite
        )
    }

}
