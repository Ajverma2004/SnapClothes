package com.ajverma.snapclothes.domain.utils

fun String.toTitleCase(): String {
    return this.lowercase()
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}
