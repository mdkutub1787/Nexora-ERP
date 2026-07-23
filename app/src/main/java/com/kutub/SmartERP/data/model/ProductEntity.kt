package com.kutub.smarterp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ProductEntity: অ্যাপের ডেটাবেসে প্রোডাক্ট (পণ্য) সংরক্ষণ করার জন্য এই ক্লাসটি ব্যবহৃত হয়।
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // ডেটাবেসের সিরিয়াল নম্বর (Auto-generated ID)

    val name: String, // পণ্যের নাম (যেমন: Laptop, Mouse)
    
    val barcode: String?, // পণ্যের গায়ে থাকা বারকোড (স্ক্যান করার জন্য)
    
    val sku: String?, // SKU = Stock Keeping Unit. এটি পণ্যের একটি ইউনিক কোড বা মডেল নম্বর যা দিয়ে সহজে প্রোডাক্ট ট্র্যাক করা যায়। (যেমন: LAP-001, MOU-X2)
    
    val price: Double, // বিক্রয় মূল্য (যে দামে কাস্টমারের কাছে বিক্রি করা হবে)
    
    val costPrice: Double, // কেনা দাম বা উৎপাদন খরচ (যে দামে আপনি কিনেছেন)
    
    val stockQuantity: Int, // স্টকে কতগুলো পণ্য আছে তার পরিমাণ
    
    val categoryId: Long? = null, // পণ্যটি কোন ক্যাটাগরির (যেমন: Electronics) তার আইডি
    
    val supplierId: Long? = null, // কোন সাপ্লায়ারের (কোম্পানি/ব্যক্তি) কাছ থেকে কেনা তার আইডি
    
    val imageUrl: String? = null, // পণ্যের ছবির লিঙ্ক বা পাথ
    
    val createdAt: Long = System.currentTimeMillis(), // কখন পণ্যটি যুক্ত করা হয়েছে তার সময়
    val updatedAt: Long = System.currentTimeMillis() // সর্বশেষ কখন পণ্যটি এডিট বা আপডেট করা হয়েছে তার সময়
)


