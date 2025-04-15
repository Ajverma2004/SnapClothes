package com.ajverma.snapclothes.presentation.screens.chatbot

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ajverma.snapclothes.data.network.models.ChatBotData
import com.ajverma.snapclothes.presentation.screens.home.productListRoute
import com.ajverma.snapclothes.presentation.utils.widgets.SnapSearchBar
import com.ajverma.snapclothes.presentation.utils.widgets.TypingAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatBotScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ChatBotViewModel = hiltViewModel()
) {

    var text by remember { mutableStateOf("") }
    val messageList = viewModel.messageList
    val context = LocalContext.current
    var isSearchFocused by remember { mutableStateOf(false) }
    var showBottomNav by rememberSaveable { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val keyboardHeightDp = with(LocalDensity.current) { imeBottomPx.toDp() }
    val isKeyboardVisible = keyboardHeightDp > 100.dp
    var wasKeyboardVisible by remember { mutableStateOf(false) }




    LaunchedEffect(currentRoute) {
        isSearchFocused = false
        isSearchActive = false
        searchText = ""
        focusManager.clearFocus()
        keyboardController?.hide()
    }
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            delay(100)
            searchFocusRequester.requestFocus()
        }
    }


    LaunchedEffect(isKeyboardVisible) {
        if (wasKeyboardVisible && !isKeyboardVisible && isSearchFocused) {
            isSearchFocused = false
            isSearchActive = false
            focusManager.clearFocus()
            keyboardController?.hide()
        }
        wasKeyboardVisible = isKeyboardVisible
    }

    BackHandler(enabled = isSearchFocused) {
        isSearchFocused = false
        isSearchActive = false
        searchText = ""
        focusManager.clearFocus()
        keyboardController?.hide()
        Log.d("BackHandler", "Unfocused instead of navigating")
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is ChatBotViewModel.ChatBotEvent.OnSendClicked -> {
                    viewModel.sendMessage(it.message)
                }

                is ChatBotViewModel.ChatBotEvent.NavigateToProductDetails -> {

                }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Messages List
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messageList.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "👋 Hi! I'm your AI assistant",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "How can I help you today?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    reverseLayout = true,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = messageList.reversed(),
                    ) { message ->
                        this@Column.AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MessageRow(message = message)
                        }
                    }
                }
            }
        }

        // Input Field
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SnapSearchBar(
                    isSearchActive = isSearchActive,
                    searchText = text,
                    onSearchTextChange = { text = it },
                    onBackClick = {

                    },
                    focusRequester = searchFocusRequester,
                    onSearchTriggered = { isSearchActive = true },
                    onFocusChanged = { isSearchFocused = it },
                    showBackButton = false,
//                    onSearchClick = {
//                        navController.navigate(productListRoute(query = searchText))
//                        searchText = ""
//                        isSearchActive = false
//                        focusManager.clearFocus()
//                        keyboardController?.hide()
//                    }
                )


                IconButton(
                    onClick = {
                        viewModel.onSendClicked(text)
                        text = ""
                    },
                    enabled = text.isNotBlank()
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<ChatBotData>
) {

    if (messageList.isEmpty()){

    }
    else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()){ messageList ->
                MessageRow(message = messageList)
            }
        }
    }
}

@Composable
fun MessageRow(
    modifier: Modifier = Modifier,
    message: ChatBotData
) {
    val clipboardManager = LocalClipboardManager.current
    var showCopyButton by remember { mutableStateOf(false) }
    val isUserMessage = message.role == "user"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier.widthIn(0.dp, 340.dp),
            color = when (message.role) {
                "user" -> Color(0xFFD1E8FF)
                else -> Color(0xFFF1F3F4)
            },
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isUserMessage) 12.dp else 4.dp,
                bottomEnd = if (isUserMessage) 4.dp else 12.dp
            ),
            tonalElevation = 1.dp
        ) {
            if (message.message == "Typing...") {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TypingAnimation(
                        circleColor = MaterialTheme.colorScheme.primary,
                        circleSize = 6f,
                        travelDistance = 6f
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showCopyButton = !showCopyButton
                            }
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                        color = if (isUserMessage) Color(0xFF00344F) else Color(0xFF333333)
                    )

                    AnimatedVisibility(
                        visible = showCopyButton,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(message.message))
                                showCopyButton = false
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Copy message",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}