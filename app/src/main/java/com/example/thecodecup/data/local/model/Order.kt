package com.example.thecodecup.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalPrice: Double,
    val timestamp: Long,
    val status: String,
   //Dung Json cho don gian
    val itemsJson: String
)