package com.ajverma.snapclothes.presentation.screens.auth

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
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
    private val googleAuthClient: GoogleAuthClient,
    private val facebookAuthClient: FacebookAuthClient
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

    fun onGoogleClick(context: ComponentActivity){
        initiateGoogleLogin(context)
    }

    private fun initiateGoogleLogin(context: ComponentActivity) {
        viewModelScope.launch {
            loading()
            facebookAuthClient.login(
                activity = context,
                onSuccess = {
                    onSocialLoginSuccess()
                },
                onCancel = {
                    onFacebookError("Facebook login canceled")
                },
                onError = {
                    onFacebookError(it)
                }
            )
        }
    }

    fun onSignOutClick(){
        viewModelScope.launch {
            Log.d("SignOutDebug", "Sign out initiated")
            googleAuthClient.signOut()
            facebookAuthClient.signOut()
            signOut()
        }
    }





    fun onFacebookClick(context: ComponentActivity){
        initiateFacebookLogin(context)
    }

    private fun initiateFacebookLogin(context: ComponentActivity){
        loading()
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    viewModelScope.launch {
                        val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) onSocialLoginSuccess()
                                else onFacebookError(task.exception?.message ?: "Firebase sign-in failed")
                            }
                    }
                }

                override fun onCancel() {
                    onFacebookError("Facebook login canceled")
                }

                override fun onError(exception: FacebookException) {
                    onFacebookError(exception.message.toString())
                    Log.e("SignUpViewModel", "Error: ${exception.message}")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(
            context,
            callbackManager,
            listOf("public_profile", "email")
        )
    }

}