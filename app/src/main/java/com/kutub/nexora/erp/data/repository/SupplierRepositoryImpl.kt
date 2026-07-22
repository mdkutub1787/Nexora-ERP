package com.kutub.nexora.erp.data.repository

import com.kutub.nexora.erp.data.local.dao.SupplierDao
import com.kutub.nexora.erp.data.model.SupplierEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao
) : SupplierRepository {
    override fun getAllSuppliers(): Flow<List<SupplierEntity>> = supplierDao.getAllSuppliers()
    
    override suspend fun getSupplierById(id: Long): SupplierEntity? = supplierDao.getSupplierById(id)
    
    override suspend fun insertSupplier(supplier: SupplierEntity): Long = supplierDao.insertSupplier(supplier)
    
    override suspend fun updateSupplier(supplier: SupplierEntity) = supplierDao.updateSupplier(supplier)
    
    override suspend fun deleteSupplier(supplier: SupplierEntity) = supplierDao.deleteSupplier(supplier)
    
    override fun getSupplierCount(): Flow<Int> = supplierDao.getSupplierCount()
}
