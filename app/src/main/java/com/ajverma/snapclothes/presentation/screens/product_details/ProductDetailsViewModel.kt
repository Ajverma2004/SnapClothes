package com.ajverma.snapclothes.presentation.screens.product_details

import androidx.lifecycle.ViewModel
import com.ajverma.snapclothes.domain.repositories.ProductDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: ProductDetailsRepository
): ViewModel()  {
}