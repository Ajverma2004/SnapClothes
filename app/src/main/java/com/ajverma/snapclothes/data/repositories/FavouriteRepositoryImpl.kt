package com.ajverma.snapclothes.data.repositories

import com.ajverma.snapclothes.database.FavouriteDao
import com.ajverma.snapclothes.database.FavouriteProduct
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dao: FavouriteDao
) {

    val favourites = dao.getAllFavourites()

    suspend fun remove(product: FavouriteProduct) = dao.removeFromFavourites(product)

    suspend fun add(product: FavouriteProduct) = dao.addToFavourites(product)

    suspend fun isFavourite(id: String) = dao.isFavourite(id)

}