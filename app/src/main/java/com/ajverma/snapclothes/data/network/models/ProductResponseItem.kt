package com.ajverma.snapclothes.data.network.models

data class ProductResponseItem(
    val _id: String,
    val arlink: String,
    val brand: String,
    val buyLink: String,
    val category: String,
    val colors_available: List<String>,
    val description: String,
    val image_urls: List<String>,
    val name: String,
    val price: Double,
    val rating: Double,
    val sizes_available: List<String>
)