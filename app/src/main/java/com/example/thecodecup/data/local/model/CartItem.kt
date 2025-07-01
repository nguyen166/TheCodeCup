package com.example.thecodecup.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coffeeId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val size: String, // "S", "M", "L"
    val shot: String, // "Single", "Double"
    val ice: String,  // "None", "Some", "Full"
    val imageResId: Int, // Lưu ID của drawable để hiển thị lại
    val address: String
)