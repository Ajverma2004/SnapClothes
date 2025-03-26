package com.ajverma.snapclothes.presentation.screens.products_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.repositories.HomeRepository
import com.ajverma.snapclothes.domain.repositories.ProductsListRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.presentation.screens.home.HomeViewModel.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val repository: ProductsListRepository,
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _state = MutableStateFlow<ProductsListState>(ProductsListState.Loading)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProductsListEvent?>()
    val event = _event.asSharedFlow()



    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            _state.emit(ProductsListState.Loading)
            val result = repository.getProductsByCategory(category)
            when (result) {
                is Resource.Success -> {
                    _state.emit(ProductsListState.Success(result.data))
                }

                is Resource.Error -> {
                    _state.emit(ProductsListState.Error(result.message))
                }
            }
        }
    }

    fun getProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProductsListState.Loading
            val result = homeRepository.getProducts()
            when (result) {
                is Resource.Success -> {
                    _state.value = ProductsListState.Success(result.data)
                }
                is Resource.Error -> {
                    _state.value = ProductsListState.Error(result.message)
                }
            }
        }
    }


    fun onBackClicked(){
        viewModelScope.launch {
            _event.emit(ProductsListEvent.OnBackClicked)
        }
    }

    fun onProductClicked(productId: String){
        viewModelScope.launch {
            _event.emit(ProductsListEvent.OnProductClicked(productId))
        }
    }


    sealed class ProductsListState {
        data object Loading : ProductsListState()
        data class Success(val products: List<ProductResponseItem>) : ProductsListState()
        data class Error(val message: String) : ProductsListState()
    }

    sealed class ProductsListEvent {
        data class OnProductClicked(val productId: String) : ProductsListEvent()
        data object OnBackClicked : ProductsListEvent()
    }

}