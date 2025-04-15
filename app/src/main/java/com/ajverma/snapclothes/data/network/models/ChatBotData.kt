package com.ajverma.snapclothes.data.network.models

data class ChatBotData(
    val message: String,
    val role: String,
    val products: List<ProductResponse>? = null
)

enum class ChatRoleEnum(val value: String) {
    USER("user"),
    MODEL("model")
}
