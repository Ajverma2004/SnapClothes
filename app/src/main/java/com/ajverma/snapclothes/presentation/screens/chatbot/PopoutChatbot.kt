package com.ajverma.snapclothes.presentation.screens.chatbot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopoutChatbot(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: ChatBotViewModel = hiltViewModel()
) {
    if (!isVisible) return

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden) onDismiss()
            true
        }
    )

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }
    val messageList = viewModel.messageList
    val listState = rememberLazyListState()
    var isInputFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    LaunchedEffect(messageList.size) {
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
                    navController.navigate(
                        com.ajverma.snapclothes.presentation.screens.navigation.ProductDetails(event.productId)
                    )
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .padding(horizontal = 12.dp)
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
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                    ) {
                        items(messageList.reversed()) { message ->
                            MessageRow(
                                message = message,
                                onProductClick = { productId ->
                                    viewModel.onProductClicked(productId)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }

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
                        focusRequester = focusRequester,
                        onFocusChanged = { isInputFocused = it }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.onSendClicked(text)
                            text = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    enabled = text.isNotBlank(),
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = Color.Black.copy(alpha = 0.3f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message"
                        )
                    }
                }
            }
        }
    }
}
