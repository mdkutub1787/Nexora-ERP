package com.kutub.smarterp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kutub.smarterp.data.model.ProductEntity
import com.kutub.smarterp.data.model.CategoryEntity
import com.kutub.smarterp.data.model.SupplierEntity
import com.kutub.smarterp.data.model.SaleEntity
import com.kutub.smarterp.data.model.SaleItemEntity
import com.kutub.smarterp.data.local.dao.CategoryDao
import com.kutub.smarterp.data.local.dao.SupplierDao
import com.kutub.smarterp.data.local.dao.SaleDao

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


