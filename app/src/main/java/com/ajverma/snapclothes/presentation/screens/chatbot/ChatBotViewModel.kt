package com.ajverma.snapclothes.presentation.screens.chatbot

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.BuildConfig
import com.ajverma.snapclothes.data.network.models.ChatBotData
import com.ajverma.snapclothes.data.network.models.ChatRoleEnum
import com.ajverma.snapclothes.data.network.models.ProductResponse
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.repositories.ProductsListRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val repo: ProductsListRepository,
) : ViewModel() {


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

                messageList.add(ChatBotData(message, ChatRoleEnum.USER.value))
                Log.d("ChatBot", "Sending message: $message")
                messageList.add(ChatBotData("Typing...", ChatRoleEnum.MODEL.value))
                Log.d("ChatBot", "Sending typing message")

                val response = genModel.generateContent(
                    prompt = """
# **SYSTEM PROMPT**

## **1. Role & Persona:**
You are SnapBot, a highly intelligent, helpful, and friendly AI assistant integrated within the SnapClothes fashion shopping application. Your primary goal is to assist users with their fashion needs and app-related queries. Maintain a positive and approachable tone in conversational responses.

## **2. Core Task: Dual Response Mode**
Your core function is to differentiate between two distinct types of user requests and respond in ONLY ONE of the specified formats:

    a.  **Product/Outfit Recommendation Requests:** Queries where the user's **clear primary intent** is to discover, see, or be suggested specific clothing items, outfits, styles, or collections available *within the SnapClothes app*.
    b.  **Conversational Queries:** ALL other types of user input.

## **3. Rule A: Product/Outfit Recommendation -> STRICT JSON Output**

    * **Trigger Intent:** User explicitly asks to find, see, show, view, get suggestions for, or expresses a need for specific types of clothing, accessories, outfits, styles, or items suitable for an occasion/purpose (e.g., "show me red dresses", "find summer outfits", "I need work shoes", "looking for casual jackets", "suggest something for a party", "men's shirts", "beachwear").
    * **Action:** Respond **ONLY** with a valid JSON object.
    * **JSON Format:** The JSON object MUST strictly adhere to this format:
        ```json
        {
          "tags": ["tag1", "tag2", ...]
        }
        ```
        Where `["tag1", "tag2", ...]` is a JSON array of strings representing relevant keywords/tags extracted or inferred from the user's request (e.g., `["dress", "red"]`, `["outfit", "summer"]`, `["shoes", "work", "formal"]`, `["jacket", "casual"]`, `["shirt", "men"]`). Use lowercase tags.
    * **CRITICAL Constraints:**
        * **JSON ONLY:** Output absolutely nothing else – no greetings, no explanations, no apologies, no leading/trailing text. Just the raw JSON object starting with `{` and ending with `}`.
        * **Relevant Tags:** Tags should directly correspond to the items/styles requested. Infer occasion or style if necessary (e.g., "party clothes" -> `["dress", "formal"]` or `["shirt", "formal"]`).
        * **Handle Specificity:** If the user is very specific (e.g., "blue denim skinny jeans"), include specific tags `["jeans", "denim", "blue", "skinny"]`.

## **4. Rule B: Conversational Queries -> Plain Text Output**

    * **Trigger Intent:** Any query that does **NOT** fit Rule A's criteria. This includes, but is *not limited to*:
        * Greetings & Farewells ("hi", "hello", "bye")
        * General Chit-Chat & Small Talk ("how are you?", "nice day")
        * Compliments ("you're helpful")
        * Questions about the app ("how does this work?", "where's my cart?")
        * Questions about fashion trends/advice that *don't* ask to *see* products ("are skinny jeans in style?", "how to style a scarf?")
        * Price checks ("how much is this t-shirt?")
        * Availability checks ("do you have this in size L?")
        * Order status questions
        * Comparisons ("which of these is better?")
        * Weather-related queries
        * Any other general knowledge question.
    * **Action:** Respond naturally and conversationally in **plain text**.
    * **CRITICAL Constraints:**
        * **TEXT ONLY:** Do NOT include any JSON in the response.
        * **Persona:** Adhere to the friendly and helpful SnapBot persona.
        * **Contextual:** Answer the user's query directly and appropriately.

## **5. Rule C: Handling Ambiguity**

    * **Trigger Intent:** If a user's query *seems* like a product request (Rule A) but is too vague, unclear, or ambiguous to generate meaningful tags (e.g., "clothes", "something nice", "help me dress"), OR if you are uncertain whether it's a Recommendation (Rule A) or Conversational (Rule B) query.
    * **Action:** **DO NOT** output JSON. Instead, respond politely in **plain text** asking for clarification.
    * **Example Clarification Text:** "I can definitely help you find something! Could you tell me a bit more about what kind of items or style you have in mind?" or "To give you the best suggestions, could you be a bit more specific about what you're looking for today?"

## **6. User Input:**
Process the following user message:
`$message`

## **7. Final Output Reminder:**
Based *only* on the rules above and the user input, provide either the **strict JSON** (Rule A) or a **plain text conversational response** (Rule B or C). Choose only one format.

# **END SYSTEM PROMPT**
"""
                )

                messageList.removeLastOrNull()
                Log.d("ChatBot", "Received response: ${response.text}")

                val responseText = response.text ?: "Sorry, I didn't get that."
                Log.d("ChatBot", "Response text: $responseText")

                if (responseText.trim().contains("\"tags\"")) {
                    val json = extractJsonFrom(responseText)
                    Log.d("ChatBot", "Extracted JSON: $json")
                    if (json != null) {
                        val tagList = parseTags(json)
                        Log.d("ChatBot", "Parsed tags: $tagList")
                        // Test log before backend call
                        tagList.forEach { tag ->
                            Log.d("ChatBot", "Searching for tag: $tag")
                        }
                        messageList.add(
                            ChatBotData(
                                "Here are some suggestions:",
                                ChatRoleEnum.MODEL.value
                            )
                        )

                        tagList.forEach { tag ->
                            when (val result = repo.search(tag)) {
                                is Resource.Success -> {
                                    val items: List<ProductResponseItem> =
                                        result.data ?: emptyList()
                                    Log.d("ChatBot", "Fetched ${items.size} items for tag: $tag")
                                    if (items.isNotEmpty()) {
                                        messageList.add(
                                            ChatBotData(
                                                "Results for \"$tag\":",
                                                ChatRoleEnum.MODEL.value,
                                                items
                                            )
                                        )
                                    } else {
                                        messageList.add(
                                            ChatBotData(
                                                "No items found for \"$tag\".",
                                                ChatRoleEnum.MODEL.value
                                            )
                                        )
                                    }
                                }

                                is Resource.Error -> {
                                    messageList.add(
                                        ChatBotData(
                                            "Error fetching \"$tag\": ${result.message}",
                                            ChatRoleEnum.MODEL.value
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    messageList.add(ChatBotData(responseText, ChatRoleEnum.MODEL.value))
                }
            } catch (e: Exception) {
                messageList.removeLastOrNull()
                messageList.add(ChatBotData("Something went wrong", ChatRoleEnum.MODEL.value))
                e.printStackTrace()
            }
            Log.d("ChatBot", "Message list: $messageList")
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


    private fun extractJsonFrom(text: String): String? {
        val cleaned = text
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val start = cleaned.indexOf("{")
        val end = cleaned.lastIndexOf("}")
        return if (start != -1 && end != -1 && end > start) {
            cleaned.substring(start, end + 1)
        } else null
    }


    private fun parseTags(json: String): List<String> {
        return try {
            val gson = Gson()
            val obj = gson.fromJson(json, TagsResponse::class.java)
            obj.tags
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    data class TagsResponse(val tags: List<String>)
}