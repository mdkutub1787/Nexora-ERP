package com.kutub.smarterp.data.repository

import com.kutub.smarterp.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductEntity>>
    fun getProductById(productId: Long): Flow<ProductEntity?>
    suspend fun getProductByBarcode(barcode: String): ProductEntity?
    suspend fun insertProduct(product: ProductEntity): Long
    suspend fun updateProduct(product: ProductEntity)
    suspend fun deleteProduct(product: ProductEntity)
    fun getProductsCount(): Flow<Int>
}


