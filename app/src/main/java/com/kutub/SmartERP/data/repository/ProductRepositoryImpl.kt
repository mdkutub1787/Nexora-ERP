package com.kutub.smarterp.data.repository

import com.kutub.smarterp.data.database.ProductDao
import com.kutub.smarterp.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    override fun getProductById(productId: Long): Flow<ProductEntity?> {
        return productDao.getProductById(productId)
    }

    override suspend fun getProductByBarcode(barcode: String): ProductEntity? {
        return productDao.getProductByBarcode(barcode)
    }

    override suspend fun insertProduct(product: ProductEntity): Long {
        return productDao.insertProduct(product)
    }

    override suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    override fun getProductsCount(): Flow<Int> {
        return productDao.getProductsCount()
    }
}


