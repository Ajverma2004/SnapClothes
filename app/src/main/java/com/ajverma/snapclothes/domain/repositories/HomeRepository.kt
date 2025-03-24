package com.ajverma.snapclothes.domain.repositories

import com.ajverma.snapclothes.data.network.models.BannerResponse
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.domain.utils.Resource

interface HomeRepository {
    suspend fun getBanners(): Resource<BannerResponse>
    suspend fun getCategories(): Resource<CategoryResponse>
}