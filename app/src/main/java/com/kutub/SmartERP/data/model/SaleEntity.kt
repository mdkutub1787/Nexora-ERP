package com.kutub.smarterp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SaleEntity: প্রতিটি বিক্রয়ের (Invoice) সাধারণ তথ্য সংরক্ষণ করে।
 */
@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // ইনভয়েস নম্বর বা সিরিয়াল
    
    val customerName: String?, // কাস্টমারের নাম (ঐচ্ছিক)
    val totalAmount: Double, // সর্বমোট বিক্রয় মূল্য (সব প্রোডাক্ট মিলে)
    val discount: Double = 0.0, // ডিসকাউন্ট (যদি থাকে)
    val finalAmount: Double, // ডিসকাউন্ট বাদ দিয়ে মূল মূল্য
    val saleDate: Long = System.currentTimeMillis() // বিক্রয়ের সময়
)


