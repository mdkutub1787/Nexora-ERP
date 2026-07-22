package com.kutub.nexora.erp.data.repository

import com.kutub.nexora.erp.data.model.SupplierEntity
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<SupplierEntity>>
    suspend fun getSupplierById(id: Long): SupplierEntity?
    suspend fun insertSupplier(supplier: SupplierEntity): Long
    suspend fun updateSupplier(supplier: SupplierEntity)
    suspend fun deleteSupplier(supplier: SupplierEntity)
    fun getSupplierCount(): Flow<Int>
}
