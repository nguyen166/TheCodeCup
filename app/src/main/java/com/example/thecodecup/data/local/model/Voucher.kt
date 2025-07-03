package com.example.thecodecup.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vouchers")
data class Voucher(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val discountType: String,
    val discountValue: Double,
    val minOrderValue: Double,
    @ColumnInfo(name = "isUsed")
    var isUsed: Boolean = false
)

val staticVoucherList = listOf(
    Voucher(
        id = 1,
        title = "15% OFF Total Order",
        description = "For orders from $5.00 and above",
        discountType = "PERCENTAGE",
        discountValue = 15.0,
        minOrderValue = 5.0
    ),
    Voucher(
        id = 2,
        title = "Free Americano",
        description = "Redeem 1 free Americano coffee",
        discountType = "FIXED_AMOUNT",
        discountValue = 2.50,
        minOrderValue = 0.0
    ),
    Voucher(
        id = 3,
        title = "$1 OFF",
        description = "Discount $1.00 for your next order",
        discountType = "FIXED_AMOUNT",
        discountValue = 1.0,
        minOrderValue = 0.0
    ),
    Voucher(
        id = 4,
        title = "20% OFF (Used)",
        description = "This voucher has been used",
        discountType = "PERCENTAGE",
        discountValue = 20.0,
        minOrderValue = 10.0,
        isUsed = true // Thêm một voucher đã dùng để test
    )
)