package com.ajverma.snapclothes.domain.repositories

import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.utils.Resource

interface ProductDetailsRepository {
    suspend fun getProductDetails(productId: String): Resource<ProductResponseItem>
}