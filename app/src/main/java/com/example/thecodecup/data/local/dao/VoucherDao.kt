package com.example.thecodecup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.thecodecup.data.local.model.Voucher
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {
    @Query("SELECT * FROM vouchers WHERE isUsed = 0 ORDER BY id DESC")
    fun getAvailableVouchers(): Flow<List<Voucher>>

    @Insert
    suspend fun insertVoucher(voucher: Voucher)

    @Update
    suspend fun updateVoucher(voucher: Voucher)
}