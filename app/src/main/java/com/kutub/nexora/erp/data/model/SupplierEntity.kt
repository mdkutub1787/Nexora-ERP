package com.kutub.nexora.erp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * SupplierEntity: আপনি যাদের কাছ থেকে পণ্য কিনবেন (কোম্পানি বা ব্যক্তি), তাদের তালিকা সংরক্ষণ করতে এটি ব্যবহৃত হয়।
 */
@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String, // সাপ্লায়ারের নাম বা কোম্পানির নাম
    val contactName: String?, // যোগাযোগের জন্য ব্যক্তির নাম (যদি কোম্পানি হয়)
    val phone: String, // মোবাইল বা ফোন নম্বর
    val email: String?, // ইমেইল ঠিকানা
    val address: String?, // ঠিকানা
    val status: Boolean = true, // সাপ্লায়ার একটিভ আছে কি না (True/False)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
