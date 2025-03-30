package com.ajverma.snapclothes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favProducts")
data class FavouriteProduct (
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val rating: Double,
    val image_urls: List<String>
)