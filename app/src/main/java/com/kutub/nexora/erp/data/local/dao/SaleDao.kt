package com.kutub.nexora.erp.data.local.dao

import androidx.room.*
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.model.SaleItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    @Insert
    suspend fun insertSaleItems(items: List<SaleItemEntity>)

    @Query("SELECT * FROM sales ORDER BY saleDate DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getSaleItemsBySaleId(saleId: Long): List<SaleItemEntity>
}
