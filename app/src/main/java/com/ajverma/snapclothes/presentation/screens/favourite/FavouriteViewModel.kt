package com.ajverma.snapclothes.presentation.screens.favourite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.repositories.FavouriteRepositoryImpl
import com.ajverma.snapclothes.database.FavouriteProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: FavouriteRepositoryImpl
): ViewModel() {

    val favourites = repository.favourites.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun toggleFavourite(product: FavouriteProduct, isFav: Boolean) {
        viewModelScope.launch {
            if (isFav) {
                repository.add(product)
                Log.d("FavouriteViewModel", "Added to favourites: ${product.name}")
            } else {
                repository.remove(product)
                Log.d("FavouriteViewModel", "Removed from favourites: ${product.name}")
            }
        }
    }

    suspend fun isFavourite(id: String) = repository.isFavourite(id)
}