package com.kutub.smarterp.data.repository

import com.kutub.smarterp.data.model.SaleEntity
import com.kutub.smarterp.data.model.SaleItemEntity
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun getAllSales(): Flow<List<SaleEntity>>
    suspend fun getSaleItemsBySaleId(saleId: Long): List<SaleItemEntity>
    
    /**
     * Performs a transaction:
     * 1. Inserts SaleEntity
     * 2. Uses the new saleId to insert all SaleItemEntities
     * 3. Reduces the stock of each product by the quantity sold
     */
    suspend fun completeSale(sale: SaleEntity, items: List<SaleItemEntity>): Long
}


