package com.ajverma.snapclothes.presentation.utils.mapper


import com.ajverma.snapclothes.R

fun iconFromCategory(category: String): Int {
    return when (category.lowercase()) {
        "ladies dress" -> R.drawable.ladies_dress
        "shirts" -> R.drawable.shirt
        "t-shirt" -> R.drawable.tshirt
        "sunglasses" -> R.drawable.sunglasses
        else -> R.drawable.other
    }
}

fun nameFromCategory(category: String): String {
    return when (category.lowercase()) {
        "ladies dress" -> "Dress"
        "shirts" -> "Shirt"
        "t-shirt" -> "T-Shirt"
        "sunglasses" -> "Sunglasses"
        else -> "others"
    }
}