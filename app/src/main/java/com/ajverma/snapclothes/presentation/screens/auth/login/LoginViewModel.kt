package com.ajverma.snapclothes.presentation.screens.auth.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
import com.ajverma.snapclothes.data.network.auth.GoogleAuthClient
import com.ajverma.snapclothes.presentation.screens.auth.AuthBaseViewModel
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionViewModel.AuthEvent
import com.ajverma.snapclothes.presentation.screens.auth.auth_option.AuthOptionViewModel.AuthNavigationEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context
): AuthBaseViewModel(
    googleAuthClient = GoogleAuthClient(context = context),
    facebookAuthClient = FacebookAuthClient()
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<LoginEvent>(LoginEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String){
        _email.value = email
    }

    fun onPasswordChange(password: String){
        _password.value = password
    }


    fun onLoginClick(){
        viewModelScope.launch {

            val emailVal = email.value.trim()

            if (emailVal.isEmpty() || password.value.isEmpty()){
                _uiState.value = LoginEvent.Error("Email and password can't be empty!")
                _navigationEvent.emit(LoginNavigationEvent.ShowDialog)
                return@launch
            }
            _uiState.value = LoginEvent.Loading
            try {
                auth.signInWithEmailAndPassword(emailVal, password.value)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            _uiState.value = LoginEvent.Success
                            viewModelScope.launch {
                                _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
                            }
                        }
                        else{
                            Log.d("SignUpDebug", "Emitting ShowDialog")
                            error = "Sign Up Failed"
                            _uiState.value = LoginEvent.Error(it.exception?.message.toString())
                            viewModelScope.launch {
                                _navigationEvent.emit(LoginNavigationEvent.ShowDialog)
                            }
                        }
                    }
            } catch (e: Exception){
                Log.d("SignUpDebug", "Emitting ShowDialog")
                error = "Sign Up Failed"
                _uiState.value = LoginEvent.Error(e.message.toString())
                _navigationEvent.emit(LoginNavigationEvent.ShowDialog)
            }
        }
    }



    override fun loading() {
        viewModelScope.launch {
            _uiState.value = LoginEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            Log.d("SignUpDebug", "Emitting ShowDialog")
            error = "Google Sign In Failed"
            _uiState.value = LoginEvent.Error(message)
            _navigationEvent.emit(LoginNavigationEvent.ShowDialog)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            Log.d("SignUpDebug", "Emitting ShowDialog")
            error = "Facebook Sign In Failed"
            _uiState.value = LoginEvent.Error(message)
            _navigationEvent.emit(LoginNavigationEvent.ShowDialog)
        }
    }
    override fun onSocialLoginSuccess() {
        viewModelScope.launch {
            _uiState.value = LoginEvent.Success
            _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
        }
    }

    override fun signOut() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToWelcomeScreen)
        }
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToSignup)
        }
    }



    sealed class LoginEvent{
        data object Success: LoginEvent()
        data object Loading: LoginEvent()
        data object Nothing: LoginEvent()
        data class Error(val message: String): LoginEvent()
    }

    sealed class LoginNavigationEvent{
        data object NavigateToWelcomeScreen: LoginNavigationEvent()
        data object NavigateToHome: LoginNavigationEvent()
        data object NavigateToSignup: LoginNavigationEvent()
        data object ShowDialog: LoginNavigationEvent()
    }

}