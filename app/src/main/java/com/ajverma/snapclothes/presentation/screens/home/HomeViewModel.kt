package com.ajverma.snapclothes.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajverma.snapclothes.data.network.models.BannerResponseItem
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.repositories.HomeRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.presentation.screens.products_list.ProductsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
): ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent?>()
    val event = _event.asSharedFlow()

    private val _banners = MutableStateFlow<List<BannerResponseItem>>(emptyList())
    val banners = _banners.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _products = MutableStateFlow<List<ProductResponseItem>>(emptyList())
    val products = _products.asStateFlow()


    init {
        getBanners()
        getCategories()
        getProducts()
    }


    fun getBanners() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = HomeState.Loading
            val result = repository.getBanners()
            when (result) {
                is Resource.Success -> {
                    _state.value = HomeState.Success
                    _banners.value = result.data
                }
                is Resource.Error -> {
                    _state.value = HomeState.Error(result.message)
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = HomeState.Loading
            val result = repository.getCategories()
            when (result) {
                is Resource.Success -> {
                    _state.value = HomeState.Success
                    _categories.value = result.data.categories
                }
                is Resource.Error -> {
                    _state.value = HomeState.Error(result.message)
                }
            }
        }
    }

    fun getProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = HomeState.Loading
            val result = repository.getProducts()
            when (result) {
                is Resource.Success -> {
                    _state.value = HomeState.Success
                    _products.value = result.data
                }
                is Resource.Error -> {
                    _state.value = HomeState.Error(result.message)
                }
            }
        }
    }

    fun onCategoryClicked(category: String) {
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigateToProductsScreen(category))
        }
    }





    sealed class HomeState {
        data object Loading : HomeState()
        data object Success : HomeState()
        data class Error(val message: String) : HomeState()
    }

    sealed class HomeEvent {
        data class NavigateToDetails(
            val id: String
        ) : HomeEvent()

        data class NavigateToProductsScreen(
            val category: String
        ): HomeEvent()
    }
}