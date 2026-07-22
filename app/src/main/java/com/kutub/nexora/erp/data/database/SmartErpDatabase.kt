package com.kutub.nexora.erp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kutub.nexora.erp.data.model.ProductEntity
import com.kutub.nexora.erp.data.model.CategoryEntity
import com.kutub.nexora.erp.data.model.SupplierEntity
import com.kutub.nexora.erp.data.model.SaleEntity
import com.kutub.nexora.erp.data.model.SaleItemEntity
import com.kutub.nexora.erp.data.local.dao.CategoryDao
import com.kutub.nexora.erp.data.local.dao.SupplierDao
import com.kutub.nexora.erp.data.local.dao.SaleDao

@Database(
    entities = [
        ProductEntity::class, 
        CategoryEntity::class, 
        SupplierEntity::class,
        SaleEntity::class,
        SaleItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SmartErpDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun supplierDao(): SupplierDao
    abstract fun saleDao(): SaleDao
}
