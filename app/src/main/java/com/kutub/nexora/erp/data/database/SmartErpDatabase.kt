package com.kutub.nexora.erp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kutub.nexora.erp.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartErpDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
