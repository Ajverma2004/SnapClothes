package com.ajverma.snapclothes.presentation.screens.auth.auth_option

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.ajverma.snapclothes.presentation.screens.navigation.SignUp
import com.ajverma.snapclothes.presentation.screens.navigation.Welcome
import com.ajverma.snapclothes.presentation.utils.BasicDialog
import com.ajverma.snapclothes.presentation.utils.auth_components.AlreadyHaveAnAccountText
import com.ajverma.snapclothes.presentation.utils.auth_components.SignInOptionButton
import com.ajverma.snapclothes.presentation.utils.auth_components.SignInTextWithLine
import com.ajverma.snapclothes.ui.theme.SnapYellow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthOptionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthOptionViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current

    var errorMessage by remember { mutableStateOf<String?>("") }
    var isLoading by remember { mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when(val state = uiState.value){
        is AuthOptionViewModel.AuthEvent.Error -> {
            isLoading = false
            errorMessage = state.message
        }
        is AuthOptionViewModel.AuthEvent.Loading -> {
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
                is AuthOptionViewModel.AuthNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home){
                        popUpTo(Welcome) {
                            inclusive = true
                        }
                    }
                }

                is AuthOptionViewModel.AuthNavigationEvent.NavigateToLogin -> {
                    navController.navigate(Login)
                }

                is AuthOptionViewModel.AuthNavigationEvent.ShowDialog -> {
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
                stringResource(R.string.welcome_to_snapclothes),
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

                // sign in options
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                        //sign in with line
                        SignInTextWithLine(
                            text = "Sign In With",
                            lineWidth = 80.dp,
                            textColor = Color.Black
                        )


                        // google and facebook buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //google button
                            SignInOptionButton(
                                onClick = {
                                    viewModel.onGoogleClick()
                                },
                                image = R.drawable.ic_google,
                                text = R.string.google,
                                elevation = 10.dp
                            )

                            //facebook button
                            SignInOptionButton(
                                onClick = {
                                    viewModel.onFacebookClick(context as ComponentActivity)
                                },
                                image = R.drawable.ic_facebook,
                                text = R.string.facebook,
                                elevation = 10.dp
                            )


                        }

                        //sign in with email or button

                        Button(
                            onClick = {
                                navController.navigate(SignUp)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black.copy(alpha = 0.1f)
                            ),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 12.dp)
                                .fillMaxWidth(),

                            ) {
                            Text(
                                "Start with email",
                                color = Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                            )
                        }


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