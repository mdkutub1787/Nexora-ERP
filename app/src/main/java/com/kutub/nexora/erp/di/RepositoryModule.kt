package com.kutub.nexora.erp.di

import com.kutub.nexora.erp.data.repository.ProductRepository
import com.kutub.nexora.erp.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.kutub.nexora.erp.data.repository.CategoryRepository
import com.kutub.nexora.erp.data.repository.CategoryRepositoryImpl
import com.kutub.nexora.erp.data.repository.SupplierRepository
import com.kutub.nexora.erp.data.repository.SupplierRepositoryImpl
import com.kutub.nexora.erp.data.repository.SaleRepository
import com.kutub.nexora.erp.data.repository.SaleRepositoryImpl

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
