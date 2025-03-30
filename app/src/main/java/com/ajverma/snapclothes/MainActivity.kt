package com.ajverma.snapclothes

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel
import com.ajverma.snapclothes.presentation.screens.home.productListRoute
import com.ajverma.snapclothes.presentation.screens.navigation.AuthOption
import com.ajverma.snapclothes.presentation.screens.navigation.Favourites
import com.ajverma.snapclothes.presentation.screens.navigation.Home
import com.ajverma.snapclothes.presentation.screens.navigation.Login
import com.ajverma.snapclothes.presentation.screens.navigation.NavRoutes
import com.ajverma.snapclothes.presentation.screens.navigation.ProductList
import com.ajverma.snapclothes.presentation.screens.navigation.SignUp
import com.ajverma.snapclothes.presentation.screens.navigation.SnapNavigation
import com.ajverma.snapclothes.presentation.screens.navigation.Welcome
import com.ajverma.snapclothes.presentation.utils.widgets.BasicDialog
import com.ajverma.snapclothes.presentation.utils.widgets.SnapSearchBar
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var showSplashScreen = true

    @Inject
    lateinit var facebookAuthClient: FacebookAuthClient

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        val homeViewModel: HomeViewModel by viewModels()

        CoroutineScope(Dispatchers.IO).launch {
            homeViewModel.getProducts()
            homeViewModel.getCategories()
            homeViewModel.getBanners()
            delay(2000)
            showSplashScreen = false
        }

        setContent {
            SnapClothesTheme {

                val navItems = listOf(
                    BottomNavItems.Home,
                    BottomNavItems.Favourites
                )


                var isSearchFocused by remember { mutableStateOf(false) }
                var showBottomNav by rememberSaveable { mutableStateOf(false) }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                var searchText by rememberSaveable { mutableStateOf("") }
                var isSearchActive by rememberSaveable { mutableStateOf(false) }
                val searchFocusRequester = remember { FocusRequester() }


                val shouldShowBackButton = when (currentRoute) {
                    Home::class.qualifiedName -> isSearchFocused
                    Favourites::class.qualifiedName -> false
                    null -> false
                    else -> true
                }

                val topBarExcludedRoutes = listOf(
                    Login::class.qualifiedName,
                    AuthOption::class.qualifiedName,
                    Welcome::class.qualifiedName,
                    SignUp::class.qualifiedName,
                )


                LaunchedEffect(currentRoute) {
                    isSearchFocused = false
                    isSearchActive = false
                    searchText = ""
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                LaunchedEffect(isSearchActive) {
                    if (isSearchActive) {
                        delay(100)
                        searchFocusRequester.requestFocus()
                    }
                }

                val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
                val keyboardHeightDp = with(LocalDensity.current) { imeBottomPx.toDp() }

                val isKeyboardVisible = keyboardHeightDp > 100.dp

                var wasKeyboardVisible by remember { mutableStateOf(false) }

                LaunchedEffect(isKeyboardVisible) {
                    if (wasKeyboardVisible && !isKeyboardVisible && isSearchFocused) {
                        isSearchFocused = false
                        isSearchActive = false
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                    wasKeyboardVisible = isKeyboardVisible
                }

                BackHandler(enabled = isSearchFocused) {
                    isSearchFocused = false
                    isSearchActive = false
                    searchText = ""
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    Log.d("BackHandler", "Unfocused instead of navigating")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute !in topBarExcludedRoutes) {
                            TopAppBar(
                                title = {
                                    SnapSearchBar(
                                        isSearchActive = isSearchActive,
                                        searchText = searchText,
                                        onSearchTextChange = { searchText = it },
                                        onBackClick = {
                                            if (isSearchFocused) {
                                                isSearchFocused = false
                                                isSearchActive = false
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            } else {
                                                if (navController.previousBackStackEntry != null) {
                                                    navController.popBackStack()
                                                }
                                            }
                                        },
                                        focusRequester = searchFocusRequester,
                                        onSearchTriggered = { isSearchActive = true },
                                        onFocusChanged = { isSearchFocused = it },
                                        showBackButton = shouldShowBackButton,
                                        onSearchClick = {
                                            navController.navigate(productListRoute(query = searchText))
                                            searchText = ""
                                            isSearchActive = false
                                            focusManager.clearFocus()
                                            keyboardController?.hide()
                                        }
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    },
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination
                        AnimatedVisibility(visible = showBottomNav) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .windowInsetsPadding(WindowInsets.Companion.navigationBars)
                                    .height(56.dp) // 👈 Shorter height
                                    .shadow(
                                        elevation = 16.dp,
                                    )
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    navItems.forEach { item ->
                                        val selected = currentRoute?.hierarchy?.any {
                                            it.route == item.route::class.qualifiedName
                                        } == true

                                        IconButton(onClick = {
                                            navController.navigate(item.route)
                                        }) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null,
                                                tint = if (selected) Color.Black else Color.Gray,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
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
                            onScreenChanged = { showBottomNav = it },
                            onBackWhileSearchFocused = {
                                if (isSearchFocused) {
                                    isSearchFocused = false
                                    isSearchActive = false
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                    Log.d("BackHandler", "Unfocused instead of navigating")
                                    true
                                } else {
                                    false
                                }
                            }
                        )
                    }
                }
            }
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
