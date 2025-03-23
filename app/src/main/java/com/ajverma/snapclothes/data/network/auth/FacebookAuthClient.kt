package com.ajverma.snapclothes.data.network.auth

import android.app.Activity
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FacebookAuthClient @Inject constructor() {

    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    fun login(
        activity: Activity,
        onSuccess: () -> Unit,
        onCancel: () -> Unit,
        onError: (String) -> Unit
    ) {
        LoginManager.getInstance().logInWithReadPermissions(
            activity,
            listOf("public_profile", "email")
        )

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FacebookAuth", "Firebase sign-in success")
                                onSuccess()
                            } else {
                                onError(task.exception?.message ?: "Firebase sign-in failed")
                            }
                        }
                }

                override fun onCancel() = onCancel()

                override fun onError(error: FacebookException) {
                    Log.e("FacebookAuth", "Facebook login failed: ${error.message}")
                    onError(error.message ?: "Facebook login failed")
                }
            })
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
    }
}
