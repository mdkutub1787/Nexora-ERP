package com.kutub.nexora.erp.data.repository

import androidx.room.withTransaction
import com.kutub.nexora.erp.data.database.ProductDao
import com.kutub.nexora.erp.data.database.SmartErpDatabase
import com.kutub.nexora.erp.data.local.dao.SaleDao
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.model.SaleItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val database: SmartErpDatabase,
    private val saleDao: SaleDao,
    private val productDao: ProductDao
) : SaleRepository {

    override fun getAllSales(): Flow<List<SaleEntity>> {
        return saleDao.getAllSales()
    }

    override suspend fun getSaleItemsBySaleId(saleId: Long): List<SaleItemEntity> {
        return saleDao.getSaleItemsBySaleId(saleId)
    }

    override suspend fun completeSale(sale: SaleEntity, items: List<SaleItemEntity>): Long {
        return database.withTransaction {
            // 1. Insert the SaleEntity
            val newSaleId = saleDao.insertSale(sale)
            
            // 2. Set the saleId on each item and insert them
            val updatedItems = items.map { it.copy(saleId = newSaleId) }
            saleDao.insertSaleItems(updatedItems)
            
            // 3. Deduct stock for each sold product
            updatedItems.forEach { item ->
                productDao.reduceStock(productId = item.productId, quantity = item.quantity)
            }
            
            newSaleId
        }
    }
}
