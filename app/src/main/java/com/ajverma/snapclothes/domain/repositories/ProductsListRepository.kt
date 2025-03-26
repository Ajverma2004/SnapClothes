package com.ajverma.snapclothes.domain.repositories

import com.ajverma.snapclothes.data.network.models.CategoryProductsResponse
import com.ajverma.snapclothes.domain.utils.Resource

interface ProductsListRepository {
    suspend fun getProductsByCategory(category: String): Resource<CategoryProductsResponse>
}