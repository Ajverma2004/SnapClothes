package com.ajverma.snapclothes.data.repositories

import com.ajverma.snapclothes.data.network.SnapApi
import com.ajverma.snapclothes.data.network.models.BannerResponse
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.domain.repositories.HomeRepository
import com.ajverma.snapclothes.domain.utils.Resource
import com.ajverma.snapclothes.domain.utils.safeApiCall
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val api: SnapApi
): HomeRepository {
    override suspend fun getBanners(): Resource<BannerResponse> {
        return safeApiCall{
            api.getBanners()
        }
    }

    override suspend fun getCategories(): Resource<CategoryResponse> {
        return safeApiCall{
            api.getCategories()
        }
    }
}