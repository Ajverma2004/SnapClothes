package com.ajverma.snapclothes.data.network.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class GoogleAuthClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val tag = "GoogleAuthClient: "

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            println(tag + "already signed in")
            return true
        }

        return false
    }

    suspend fun signInWithGoogle(): SignInResult {
        if (isSignedIn()) {
            return SignInResult.Success
        }

        return try {
            val result = buildCredentialRequest()
            val credential = result.credential

            if (
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                if (authResult.user != null) {
                    SignInResult.Success
                } else {
                    SignInResult.Error("Sign-in failed: user is null.")
                }
            } else {
                SignInResult.Error("Invalid Google credentials.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult.Error("Google Sign-in error: ${e.localizedMessage}")
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        "395194503910-11crd21hl11p09gfdlpi3hnbl745238a.apps.googleusercontent.com"
                    )
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(
            request = request, context = context
        )
    }

    suspend fun signOut() {
        Log.d(tag, "Signing out from Firebase and clearing credentials")
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        firebaseAuth.signOut()
        Log.d(tag, "Signed out from Firebase")
    }
}

sealed class SignInResult {
    data object Success : SignInResult()
    data class Error(val message: String) : SignInResult()
}