package com.ajverma.snapclothes.presentation.screens.chatbot

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.data.network.models.ChatBotData
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.presentation.screens.home.productListRoute
import com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails
import com.ajverma.snapclothes.presentation.utils.widgets.SnapSearchBar
import com.ajverma.snapclothes.presentation.utils.widgets.TypingAnimation
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatBotScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ChatBotViewModel = hiltViewModel()
) {


    var text by rememberSaveable { mutableStateOf("") }
    val messageList = viewModel.messageList
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchFocusRequester = remember { FocusRequester() }
    var isInputFocused by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeBottomPx > 0

    LaunchedEffect(messageList.size, isKeyboardVisible) {
        if (messageList.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(0)
        }
    }

    BackHandler(enabled = isInputFocused) {
        isInputFocused = false
        focusManager.clearFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ChatBotViewModel.ChatBotEvent.OnSendClicked -> {
                    viewModel.sendMessage(event.message)
                }
                is ChatBotViewModel.ChatBotEvent.NavigateToProductDetails -> {
                    navController.navigate(ProductDetails(event.productId))
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ChatInputField(
                        text = text,
                        onTextChange = { text = it },
                        focusRequester = searchFocusRequester,
                        onFocusChanged = { isInputFocused = it }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                val haptic = LocalHapticFeedback.current
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.onSendClicked(text)
                            text = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = text.isNotBlank(),
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = Color.Black.copy(alpha = 0.3f)
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        if (isInputFocused) {
                            focusManager.clearFocus()
                            isInputFocused = false
                        }
                    })
                }
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messageList.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        reverseLayout = true,
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        items(
                            items = messageList.reversed(),
                        ) { message ->
                            MessageRow(
                                message = message,
                                onProductClick = { productId ->
                                    viewModel.onProductClicked(productId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ai_chatbot_2),
            contentDescription = "AI Chatbot",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "👋 Hi! I'm your AI Assistant",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "How can I help you today?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}

@Composable
fun MessageRow(
    modifier: Modifier = Modifier,
    message: ChatBotData,
    onProductClick: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val isUserMessage = message.role == "user"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            clipboardManager.setText(AnnotatedString(message.message))
                        }
                    )
                },
            color = when (message.role) {
                "user" -> MaterialTheme.colorScheme.primary
                else -> Color.Transparent
            },
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isUserMessage) 12.dp else 4.dp,
                bottomEnd = if (isUserMessage) 4.dp else 12.dp
            ),
            tonalElevation = if (isUserMessage) 4.dp else 0.dp,
            shadowElevation = if (isUserMessage) 4.dp else 0.dp
        ) {
            Column(
                modifier = Modifier.padding(
                    vertical = if (message.message == "Typing...") 4.dp else 8.dp,
                    horizontal = 12.dp
                )
            ) {
                if (message.message == "Typing...") {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .height(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TypingAnimation(
                            circleColor = Color.Black,
                            circleSize = 6f,
                            travelDistance = 6f
                        )
                    }
                } else {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp),
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    if (!message.products.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            message.products.forEach { product ->
                                ProductCard(product = product) {
                                    onProductClick(product._id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ChatInputField(
    text: String,
    onTextChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = {
            Text(
                text = "Type a message...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .height(48.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default)
    )
}



@Composable
fun ProductCard(
    product: ProductResponseItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image_urls.firstOrNull(),
                contentDescription = "Product Image: ${product.name}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "£${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}