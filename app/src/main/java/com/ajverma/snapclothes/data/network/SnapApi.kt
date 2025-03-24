package com.ajverma.snapclothes.data.network

import com.ajverma.snapclothes.data.network.models.BannerResponse
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import retrofit2.http.GET

interface SnapApi {

    @GET("banners")
    suspend fun getBanners(): BannerResponse

    @GET("categories")
    suspend fun getCategories(): CategoryResponse
}