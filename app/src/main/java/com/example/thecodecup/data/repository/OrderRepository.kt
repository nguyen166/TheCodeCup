package com.example.thecodecup.data.repository

import com.example.thecodecup.data.local.dao.OrderDao
import com.example.thecodecup.data.local.model.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val orderDao: OrderDao) {
    fun getOngoingOrders(): Flow<List<Order>> {
        return orderDao.getOngoingOrders()
    }
    fun getHistoryOrders(): Flow<List<Order>> {
        return orderDao.getHistoryOrders()
    }

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    fun getLatestOrder(): Flow<Order?> {
        return orderDao.getLatestOrder()
    }
}