package com.ajverma.snapclothes.presentation.screens.auth.sign_up

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
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
class SignupViewModel @Inject constructor(
    @ApplicationContext context: Context
): AuthBaseViewModel(
    googleAuthClient = GoogleAuthClient(context = context)
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<SignupEvent>(SignupEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignupNavigationEvent>()
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


    fun onSignUpClick(){
        viewModelScope.launch {

            val emailVal = email.value.trim()

            if (emailVal.isEmpty() || password.value.isEmpty()){
                _uiState.value = SignupEvent.Error("Email and password can't be empty!")
                _navigationEvent.emit(SignupNavigationEvent.ShowDialog)
                return@launch
            }
            _uiState.value = SignupEvent.Loading
            try {
                auth.createUserWithEmailAndPassword(emailVal, password.value)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            _uiState.value = SignupEvent.Success
                            viewModelScope.launch {
                                _navigationEvent.emit(SignupNavigationEvent.NavigateToHome)
                            }
                        }
                        else{
                            Log.d("SignUpDebug", "Emitting ShowDialog")
                            error = "Sign Up Failed"
                            _uiState.value = SignupEvent.Error(it.exception?.message.toString())
                            viewModelScope.launch {
                                _navigationEvent.emit(SignupNavigationEvent.ShowDialog)
                            }
                        }
                    }
            } catch (e: Exception){
                Log.d("SignUpDebug", "Emitting ShowDialog")
                error = "Sign Up Failed"
                _uiState.value = SignupEvent.Error(e.message.toString())
                _navigationEvent.emit(SignupNavigationEvent.ShowDialog)
            }
        }
    }



    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            Log.d("SignUpDebug", "Emitting ShowDialog")
            error = "Google Sign In Failed"
            _uiState.value = SignupEvent.Error(message)
            _navigationEvent.emit(SignupNavigationEvent.ShowDialog)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            Log.d("SignUpDebug", "Emitting ShowDialog")
            error = "Facebook Sign In Failed"
            _uiState.value = SignupEvent.Error(message)
            _navigationEvent.emit(SignupNavigationEvent.ShowDialog)
        }
    }
    override fun onSocialLoginSuccess() {
        viewModelScope.launch {
            _uiState.value = SignupEvent.Success
            _navigationEvent.emit(SignupNavigationEvent.NavigateToHome)
        }
    }

    override fun signOut() {
        viewModelScope.launch {
            _navigationEvent.emit(SignupNavigationEvent.NavigateToWelcomeScreen)
        }
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(SignupNavigationEvent.NavigateToLogin)
        }
    }



    sealed class SignupEvent{
        data object Success: SignupEvent()
        data object Loading: SignupEvent()
        data object Nothing: SignupEvent()
        data class Error(val message: String): SignupEvent()
    }

    sealed class SignupNavigationEvent{
        data object NavigateToWelcomeScreen: SignupNavigationEvent()
        data object NavigateToHome: SignupNavigationEvent()
        data object NavigateToLogin: SignupNavigationEvent()
        data object ShowDialog: SignupNavigationEvent()
    }

}