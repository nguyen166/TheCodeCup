package com.example.thecodecup.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thecodecup.data.local.dao.*
import com.example.thecodecup.data.local.model.*

@Database(
    entities = [CartItem::class, Order::class, UserProfile::class, Voucher::class],
    version = 1,
    exportSchema = false //false de don gian cho project nay
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun profileDao(): UserProfileDao
    abstract fun voucherDao(): VoucherDao
}