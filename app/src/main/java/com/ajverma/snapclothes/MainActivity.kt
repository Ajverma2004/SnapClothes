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
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu // Import Menu icon
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState // Import drawer state
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajverma.snapclothes.presentation.screens.chatbot.FloatingChatButton
import com.ajverma.snapclothes.presentation.screens.chatbot.PopoutChatbot
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel
import com.ajverma.snapclothes.presentation.screens.home.productListRoute
import com.ajverma.snapclothes.presentation.screens.navigation.AuthOption
import com.ajverma.snapclothes.presentation.screens.navigation.Carousal
import com.ajverma.snapclothes.presentation.screens.navigation.ChatBot
import com.ajverma.snapclothes.presentation.screens.navigation.Favourites
import com.ajverma.snapclothes.presentation.screens.navigation.Home
import com.ajverma.snapclothes.presentation.screens.navigation.Login
import com.ajverma.snapclothes.presentation.screens.navigation.NavRoutes
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.ajverma.snapclothes.presentation.screens.navigation.ProductList
import com.ajverma.snapclothes.presentation.screens.navigation.SignUp
import com.ajverma.snapclothes.presentation.screens.navigation.SnapNavigation
import com.ajverma.snapclothes.presentation.screens.navigation.Welcome
import com.ajverma.snapclothes.presentation.utils.widgets.BasicDialog
import com.ajverma.snapclothes.presentation.utils.widgets.SnapSearchBar
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
import com.google.firebase.auth.FirebaseAuth
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
            delay(2000)
            showSplashScreen = false
        }

        setContent {
            SnapClothesTheme {
                val navItems = listOf(
                    BottomNavItems.Home,
                    BottomNavItems.Carousal,
                    BottomNavItems.Favourites
                )

                val bottomNavRoutes = listOf(Home, Favourites)
                val previousBottomRoute = remember { mutableStateOf<NavRoutes?>(null) }

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

                var isChatbotVisible by remember { mutableStateOf(false) }

                val destination = navController.currentBackStackEntryAsState().value?.destination
                val fabPosition = remember(destination) {
                    if (destination?.route?.startsWith("productDetails") == true) {
                        FabPosition.Start
                    } else {
                        FabPosition.End
                    }
                }

                val authRoutes = listOf(
                    Login::class.qualifiedName,
                    AuthOption::class.qualifiedName,
                    Welcome::class.qualifiedName,
                    SignUp::class.qualifiedName,
                    "com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails/{productId}",
                    Carousal::class.qualifiedName
                )

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
                    ChatBot::class.qualifiedName,
                    Carousal::class.qualifiedName
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

                // Navigation Drawer State
                val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val isDrawerEnabled = remember(currentRoute) {
                    (currentRoute == Home::class.qualifiedName || currentRoute == Favourites::class.qualifiedName) &&
                            currentRoute !in authRoutes
                }

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = isDrawerEnabled,
                    drawerContent = {
                        DrawerContent(onLogout = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Welcome){
                                popUpTo(0){
                                    inclusive = true
                                }
                            }
                        }, drawerState = drawerState)
                    }
                ) { // This is the content of the ModalNavigationDrawer

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
                                    ),
                                    navigationIcon = {
                                        if ((currentRoute == Home::class.qualifiedName || currentRoute == Favourites::class.qualifiedName) &&
                                            currentRoute !in authRoutes
                                        ) {
                                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                                Icon(Icons.Filled.Menu, contentDescription = "Open navigation drawer")
                                            }
                                        }
                                    }
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

                                            val isMiddle = item == BottomNavItems.Carousal

                                            // Idle animation only for AR Button
                                            val infiniteTransition = rememberInfiniteTransition(label = "ARAnimation")
                                            val bounceOffset by infiniteTransition.animateFloat(
                                                initialValue = 0f,
                                                targetValue = if (isMiddle) -6f else 0f,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(1000, easing = FastOutLinearInEasing, delayMillis = 1000),
                                                    repeatMode = RepeatMode.Reverse
                                                ),
                                                label = "ARButtonBounce"
                                            )

                                            IconButton(onClick = {
                                                val routeString = currentRoute?.route
                                                previousBottomRoute.value = bottomNavRoutes.find { it::class.qualifiedName == routeString }

                                                navController.navigate(item.route) {
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }

                                            },
                                                modifier = if (isMiddle) Modifier.offset(y = bounceOffset.dp) else Modifier
                                            ) {
                                                Icon(
                                                    painter = painterResource(item.icon),
                                                    contentDescription = null,
                                                    tint = if (selected) Color.Black else Color.Gray,
                                                    modifier = Modifier.size(
                                                        if(item.icon == R.drawable.ar_hehe) 32.dp else 24.dp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                            }
                        },
                        floatingActionButtonPosition = fabPosition,
                        floatingActionButton = {
                            if (currentRoute !in authRoutes) {
                                FloatingChatButton(onChatButtonClicked = { isChatbotVisible = !isChatbotVisible })
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(innerPadding),
                            contentAlignment = Alignment.BottomEnd
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
                            // Show the pop-out chatbot when isChatbotVisible is true
                            if (isChatbotVisible) {
                                PopoutChatbot(
                                    isVisible = isChatbotVisible,
                                    onDismiss = { isChatbotVisible = false },
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DrawerContent(onLogout: () -> Unit, drawerState: androidx.compose.material3.DrawerState) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.material3.Button(onClick = {
                onLogout()
                scope.launch { drawerState.close() }
            }) {
                Text("Logout")
            }
        }
    }

    sealed class BottomNavItems(val route: NavRoutes, val icon: Int) {
        data object Home : BottomNavItems(
            com.ajverma.snapclothes.presentation.screens.navigation.Home,
            R.drawable.homebn
        )

        data object Favourites : BottomNavItems(
            com.ajverma.snapclothes.presentation.screens.navigation.Favourites,
            R.drawable.favourite_bn
        )

        data object Carousal : BottomNavItems(
            com.ajverma.snapclothes.presentation.screens.navigation.Carousal,
            R.drawable.ar_hehe
        )
    }
}