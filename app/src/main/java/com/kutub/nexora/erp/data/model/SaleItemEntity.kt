package com.kutub.nexora.erp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * SaleItemEntity: একটি নির্দিষ্ট ইনভয়েসে (Sale) কী কী প্রোডাক্ট বিক্রি হয়েছে তার তথ্য।
 */
@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["saleId"]),
        Index(value = ["productId"])
    ]
)
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val saleId: Long, // কোন ইনভয়েসের আন্ডারে বিক্রি হচ্ছে
    val productId: Long, // কোন প্রোডাক্ট বিক্রি হচ্ছে
    val productName: String, // প্রোডাক্টের নাম (ভবিষ্যতে প্রোডাক্ট ডিলিট হলেও নামটা থাকার জন্য)
    val quantity: Int, // কতগুলো বিক্রি হলো
    val unitPrice: Double, // প্রতি পিসের দাম
    val totalPrice: Double // মোট দাম (quantity * unitPrice)
)
