package com.ajverma.snapclothes.domain.repositories

import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.data.network.models.ProductResponse
import com.ajverma.snapclothes.domain.utils.Resource

interface HomeRepository {
    suspend fun getCategories(): Resource<CategoryResponse>
    suspend fun getProducts(): Resource<ProductResponse>

}