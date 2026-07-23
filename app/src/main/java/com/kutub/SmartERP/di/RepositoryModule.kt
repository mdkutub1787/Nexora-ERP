package com.kutub.smarterp.di

import com.kutub.smarterp.data.repository.ProductRepository
import com.kutub.smarterp.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.kutub.smarterp.data.repository.CategoryRepository
import com.kutub.smarterp.data.repository.CategoryRepositoryImpl
import com.kutub.smarterp.data.repository.SupplierRepository
import com.kutub.smarterp.data.repository.SupplierRepositoryImpl
import com.kutub.smarterp.data.repository.SaleRepository
import com.kutub.smarterp.data.repository.SaleRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindSupplierRepository(
        supplierRepositoryImpl: SupplierRepositoryImpl
    ): SupplierRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(
        saleRepositoryImpl: SaleRepositoryImpl
    ): SaleRepository
}


