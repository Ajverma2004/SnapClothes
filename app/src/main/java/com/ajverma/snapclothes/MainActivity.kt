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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
import com.ajverma.snapclothes.presentation.screens.navigation.SnapNavigation
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme
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

                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ){
                        SnapNavigation(navController = navController)
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            showSplashScreen = false
        }
    }

}
