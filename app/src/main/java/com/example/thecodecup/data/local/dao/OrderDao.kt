package com.example.thecodecup.data.local.dao

import androidx.room.*
import com.example.thecodecup.data.local.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders WHERE status = 'ongoing' ORDER BY timestamp DESC")
    fun getOngoingOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE status = 'history' ORDER BY timestamp DESC")
    fun getHistoryOrders(): Flow<List<Order>>

    @Update
    suspend fun updateOrder(order: Order)
}