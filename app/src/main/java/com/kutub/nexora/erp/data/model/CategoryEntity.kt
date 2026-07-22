package com.kutub.nexora.erp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CategoryEntity: পণ্যের ক্যাটাগরি (যেমন: Electronics, Grocery, Clothing) সংরক্ষণ করার জন্য ব্যবহৃত হয়।
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String, // ক্যাটাগরির নাম
    val description: String?, // ক্যাটাগরির বিস্তারিত বিবরণ
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
