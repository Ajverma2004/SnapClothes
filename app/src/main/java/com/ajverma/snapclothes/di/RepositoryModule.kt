package com.ajverma.snapclothes.di

import com.ajverma.snapclothes.data.repositories.HomeRepositoryImpl
import com.ajverma.snapclothes.data.repositories.ProductDetailsRepositoryImpl
import com.ajverma.snapclothes.data.repositories.ProductsListRepositoryImpl
import com.ajverma.snapclothes.domain.repositories.HomeRepository
import com.ajverma.snapclothes.domain.repositories.ProductDetailsRepository
import com.ajverma.snapclothes.domain.repositories.ProductsListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindProductsListRepository(
        productsListRepositoryImpl: ProductsListRepositoryImpl
    ): ProductsListRepository

    @Binds
    @Singleton
    abstract fun bindProductDetailsRepository(
        productDetailsRepositoryImpl: ProductDetailsRepositoryImpl
    ): ProductDetailsRepository

}