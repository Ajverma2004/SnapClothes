package com.ajverma.snapclothes.data.network

import com.ajverma.snapclothes.data.network.models.BannerResponse
import com.ajverma.snapclothes.data.network.models.CategoryProductsResponse
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import com.ajverma.snapclothes.data.network.models.ProductResponse
import com.ajverma.snapclothes.data.network.models.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SnapApi {

    @GET("banners")
    suspend fun getBanners(): BannerResponse

    @GET("categories")
    suspend fun getCategories(): CategoryResponse

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("category/{category}")
    suspend fun getProductsByCategory(
        @Path("category") category: String
    ): CategoryProductsResponse

    @GET("product/{productId}")
    suspend fun searchProducts(
        @Path("productId") productId: String
    ): ProductResponseItem

    @GET("search")
    suspend fun search(
        @Query("query") query: String
    ): ProductResponse


}