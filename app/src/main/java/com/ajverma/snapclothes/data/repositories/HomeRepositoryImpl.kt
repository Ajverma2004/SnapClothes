package com.ajverma.snapclothes.data.repositories

import com.ajverma.snapclothes.data.network.SnapApi
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.data.network.models.ProductResponse
import com.ajverma.snapclothes.domain.repositories.HomeRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.domain.utils.safeApiCall
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val api: SnapApi
): HomeRepository {

    override suspend fun getCategories(): Resource<CategoryResponse> {
        return safeApiCall{
            api.getCategories()
        }
    }

    override suspend fun getProducts(): Resource<ProductResponse> {
        return safeApiCall{
            api.getProducts()
        }
    }
}