package com.ajverma.snapclothes.presentation.screens.auth

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.auth.GoogleAuthClient
import com.ajverma.snapclothes.data.network.auth.SignInResult
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class AuthBaseViewModel(
    private val googleAuthClient: GoogleAuthClient
): ViewModel() {

    var error: String = ""
    private lateinit var callbackManager: CallbackManager


    abstract fun loading()
    abstract fun onGoogleError(message: String)
    abstract fun onFacebookError(message: String)
    abstract fun onSocialLoginSuccess()
    abstract fun signOut()

    fun isSignedIn(): Boolean {
        return googleAuthClient.isSignedIn()
    }

    fun onGoogleClick(activity: ComponentActivity){
        initiateGoogleLogin(activity)
    }

    private fun initiateGoogleLogin(activity: ComponentActivity) {
        viewModelScope.launch {
            loading()
            try {
                val result = googleAuthClient.signInWithGoogle(activity)
                when (result) {
                    is SignInResult.Success -> {
                        Log.d("GoogleAuthClient", "Sign in successful")
                        onSocialLoginSuccess()
                    }
                    is SignInResult.Error -> {
                        onGoogleError(result.message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onGoogleError(e.message ?: "Unknown error")
            }

        }
    }

    fun onSignOutClick(){
        viewModelScope.launch {
            Log.d("SignOutDebug", "Sign out initiated")
            googleAuthClient.signOut()
            signOut()
        }
    }
}