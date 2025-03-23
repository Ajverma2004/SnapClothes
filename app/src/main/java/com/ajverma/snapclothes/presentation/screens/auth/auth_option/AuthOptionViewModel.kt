package com.ajverma.snapclothes.presentation.screens.auth.auth_option

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.auth.FacebookAuthClient
import com.ajverma.snapclothes.data.network.auth.GoogleAuthClient
import com.ajverma.snapclothes.presentation.screens.auth.AuthBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthOptionViewModel @Inject constructor(
    @ApplicationContext context: Context
): AuthBaseViewModel(
    googleAuthClient = GoogleAuthClient(context = context),
    facebookAuthClient = FacebookAuthClient()
) {

    private val _uiState = MutableStateFlow<AuthEvent>(AuthEvent.Loading)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    override fun loading() {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            error = "Google Sign In Failed"
            _uiState.value = AuthEvent.Error(message)
            _navigationEvent.emit(AuthNavigationEvent.ShowDialog)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            error = "Facebook Sign In Failed"
            _uiState.value = AuthEvent.Error(message)
            _navigationEvent.emit(AuthNavigationEvent.ShowDialog)
        }
    }
    override fun onSocialLoginSuccess() {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Success
            _navigationEvent.emit(AuthNavigationEvent.NavigateToHome)
        }
    }

    override fun signOut() {
        viewModelScope.launch {
            _navigationEvent.emit(AuthNavigationEvent.NavigateToWelcomeScreen)
        }
    }

    fun navigateToLogin(){
        viewModelScope.launch {
            _navigationEvent.emit(AuthNavigationEvent.NavigateToLogin)
        }

    }


    sealed class AuthEvent{
        data class Error(val message: String): AuthEvent()
        data object Loading: AuthEvent()
        data object Success: AuthEvent()
    }

    sealed class AuthNavigationEvent{
        data object NavigateToWelcomeScreen: AuthNavigationEvent()
        data object NavigateToHome: AuthNavigationEvent()
        data object NavigateToLogin: AuthNavigationEvent()
        data object ShowDialog: AuthNavigationEvent()
    }
}