package com.ajverma.snapclothes.data.repositories

import com.ajverma.snapclothes.data.network.SnapApi
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import com.ajverma.snapclothes.domain.repositories.ProductDetailsRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.domain.utils.safeApiCall
import javax.inject.Inject

class ProductDetailsRepositoryImpl @Inject constructor(
    private val api: SnapApi
): ProductDetailsRepository {
    override suspend fun getProductDetails(productId: String): Resource<ProductResponseItem> {
        return safeApiCall {
            api.searchProducts(productId)
        }
    }
}