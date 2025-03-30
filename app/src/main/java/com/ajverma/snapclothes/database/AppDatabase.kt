package com.ajverma.snapclothes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ajverma.snapclothes.domain.utils.Converters

@Database(entities = [FavouriteProduct::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}