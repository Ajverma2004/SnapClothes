package com.ajverma.snapclothes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(product: FavouriteProduct)

    @Delete
    suspend fun removeFromFavourites(product: FavouriteProduct)

    @Query("SELECT * FROM favProducts")
    fun getAllFavourites(): Flow<List<FavouriteProduct>>

    @Query("SELECT EXISTS(SELECT * FROM favProducts WHERE id = :id)")
    suspend fun isFavourite(id: String): Boolean

}