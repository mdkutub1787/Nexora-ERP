package com.kutub.nexora.erp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcode: String?,
    val sku: String?,
    val price: Double,
    val costPrice: Double,
    val stockQuantity: Int,
    val categoryId: Long? = null,
    val supplierId: Long? = null,
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
