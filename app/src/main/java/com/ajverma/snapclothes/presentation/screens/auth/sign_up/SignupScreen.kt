package com.ajverma.snapclothes.presentation.screens.auth.sign_up

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.presentation.screens.navigation.AuthOption
import com.ajverma.snapclothes.presentation.screens.navigation.Home
import com.ajverma.snapclothes.presentation.screens.navigation.Login
import com.ajverma.snapclothes.presentation.screens.navigation.Welcome
import com.ajverma.snapclothes.presentation.utils.widgets.BasicDialog
import com.ajverma.snapclothes.presentation.utils.widgets.SnapClothesTextField
import com.ajverma.snapclothes.presentation.utils.auth_components.AlreadyHaveAnAccountText
import com.ajverma.snapclothes.presentation.utils.auth_components.SignInOptionButton
import com.ajverma.snapclothes.presentation.utils.auth_components.SignInTextWithLine
import com.ajverma.snapclothes.ui.theme.SnapYellow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()


    val context = LocalContext.current

    var errorMessage by remember { mutableStateOf<String?>("") }
    var isLoading by remember { mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when(val state = uiState.value){
        is SignupViewModel.SignupEvent.Error -> {
            isLoading = false
            errorMessage = state.message
        }
        is SignupViewModel.SignupEvent.Loading -> {
            isLoading = true
            errorMessage = null
        }
        else -> {
            isLoading = false
            errorMessage = null
        }
    }

    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when(event){
                is SignupViewModel.SignupNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home){
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                is SignupViewModel.SignupNavigationEvent.NavigateToLogin -> {
                    navController.navigate(Login){
                        popUpTo(AuthOption) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is SignupViewModel.SignupNavigationEvent.ShowDialog -> {
                    showDialog = true
                }
                else -> {}
            }

        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SnapYellow)
    ) {

        // welcome text
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .size(90.dp)
            )

            Text(
                stringResource(R.string.sign_up),
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier,
            )
        }



        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomCenter)
            ) {

                SnapClothesTextField(
                    value = email.value, onValueChange = { viewModel.onEmailChange(it) },
                    label = {
                        Text(text = stringResource(id = R.string.email), color = Color.Black)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SnapClothesTextField(
                    value = password.value, onValueChange = { viewModel.onPasswordChange(it) },
                    label = {
                        Text(text = stringResource(id = R.string.password), color = Color.Black)
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                //sign up with email button

                Button(
                    onClick = {
                        viewModel.onSignUpClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp,  Color.Black),
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                        .fillMaxWidth(),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Box{
                        AnimatedContent(targetState = isLoading,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) togetherWith
                                        fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                            }
                        ){ target ->
                            if (target) {
                                CircularProgressIndicator(
                                    color = Color.Black,
                                    modifier = Modifier
                                        .padding(horizontal = 32.dp)
                                        .size(24.dp)
                                )
                            } else{
                                Text(
                                    "SIGN UP",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(25.dp))



                // sign in options
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    //sign in with line
                    SignInTextWithLine(
                        text = "Sign In With",
                        lineWidth = 80.dp,
                        textColor = Color.Black
                    )


                    // google button
                    SignInOptionButton(
                        onClick = {
                            viewModel.onGoogleClick(context as ComponentActivity)
                        },
                        image = R.drawable.ic_google,
                        text = R.string.google,
                        elevation = 10.dp,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    // already have an account text
                    AlreadyHaveAnAccountText(
                        trailingText = "Login",
                        textDecoration = TextDecoration.Underline,
                        onClick = {
                            viewModel.navigateToLogin()
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = { showDialog = false },
            sheetState = sheetState
        ) {
            BasicDialog(
                title = viewModel.error,
                description = errorMessage.toString(),
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        showDialog = false
                    }
                }
            )
        }
    }

}