package com.example.thecodecup.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalPrice: Double,
    val timestamp: Long,
    val status: String,
   //Dung Json cho don gian
    val itemsJson: String,
    val pointsAwarded: Int,
    val address: String
){
    @Ignore
    val items: List<OrderItem> = try {
        Json.decodeFromString<List<OrderItem>>(itemsJson)
    } catch (e: Exception) {
        emptyList()
    }
}

@Serializable
data class OrderItem(
    val coffeeId: Int,
    val name: String,
    val quantity: Int,
    val size: String,
    val shot: String,
    val ice: String,
    val pricePerItem: Double,
)


