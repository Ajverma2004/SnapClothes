package com.ajverma.snapclothes.presentation.screens.chatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.BuildConfig
import com.ajverma.snapclothes.data.network.models.ChatBotData
import com.ajverma.snapclothes.data.network.models.ChatRoleEnum
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor() : ViewModel() {


    private val _event = MutableSharedFlow<ChatBotEvent>()
    val event = _event.asSharedFlow()

    val messageList by lazy {
        mutableStateListOf<ChatBotData>()
    }

    private val genModel: GenerativeModel = GenerativeModel(
        "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val chat = genModel.startChat(
                    history = messageList.map {
                        content(it.role) {
                            text(it.message)
                        }
                    }.toList()
                )

                messageList.add(ChatBotData(message, ChatRoleEnum.USER.value))
                messageList.add(ChatBotData("Typing...", ChatRoleEnum.MODEL.value))

                val response = chat.sendMessage(message)
                messageList.removeLastOrNull()
                messageList.add(ChatBotData(response.text.toString(), ChatRoleEnum.MODEL.value))

            } catch (e: Exception) {
                messageList.removeLastOrNull()
                messageList.add(ChatBotData("Something went wrong", ChatRoleEnum.MODEL.value))
                e.printStackTrace()
            }
        }
    }

    fun onSendClicked(message: String) {
        viewModelScope.launch {
            _event.emit(ChatBotEvent.OnSendClicked(message))
        }
    }

    fun onProductClicked(productId: String) {
        viewModelScope.launch {
            _event.emit(ChatBotEvent.NavigateToProductDetails(productId))
        }
    }



    sealed class ChatBotEvent {
        data class OnSendClicked(val message: String) : ChatBotEvent()
        data class NavigateToProductDetails(val productId: String) : ChatBotEvent()
    }
}