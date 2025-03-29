package com.ajverma.snapclothes.data.repositories

import com.ajverma.snapclothes.data.network.SnapApi
import com.ajverma.snapclothes.data.network.models.CategoryProductsResponse
import com.ajverma.snapclothes.data.network.models.ProductResponse
import com.ajverma.snapclothes.domain.repositories.ProductsListRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.domain.utils.safeApiCall
import javax.inject.Inject

class ProductsListRepositoryImpl @Inject constructor(
    private val api: SnapApi
): ProductsListRepository {
    override suspend fun getProductsByCategory(category: String): Resource<CategoryProductsResponse> {
        return safeApiCall{
            api.getProductsByCategory(category)
        }
    }

    override suspend fun search(query: String): Resource<ProductResponse> {
        return safeApiCall {
            api.search(query)
        }
    }
}