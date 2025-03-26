package com.ajverma.snapclothes.presentation.screens.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.repositories.ProductDetailsRepository
import com.ajverma.snapclothes.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductDetailsRepository
): ViewModel()  {

    private val _state = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProductDetailsEvent>()
    val event = _event.asSharedFlow()


    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            val result = repository.getProductDetails(productId)
            when (result) {
                is Resource.Success -> {
                    _state.value = ProductDetailsState.Success(result.data)
                }
                is Resource.Error -> {
                    _state.value = ProductDetailsState.Error(result.message)
                }
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _event.emit(ProductDetailsEvent.NavigateBack)
        }
    }

    sealed class ProductDetailsState {
        data object Loading : ProductDetailsState()
        data class Success(val data: ProductResponseItem) : ProductDetailsState()
        data class Error(val message: String) : ProductDetailsState()
    }

    sealed class ProductDetailsEvent {
        data object NavigateBack : ProductDetailsEvent()
    }
}