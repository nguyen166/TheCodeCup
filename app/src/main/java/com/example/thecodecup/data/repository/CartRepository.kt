package com.example.thecodecup.data.repository

import com.example.thecodecup.data.local.dao.CartDao
import com.example.thecodecup.data.local.model.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val cartDao: CartDao) {

    fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    suspend fun addToCart(item: CartItem) {
        cartDao.insertCartItem(item)
    }

    suspend fun removeFromCart(item: CartItem) {
        cartDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}