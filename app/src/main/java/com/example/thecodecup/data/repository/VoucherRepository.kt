package com.example.thecodecup.data.repository

import com.example.thecodecup.data.local.dao.VoucherDao
import com.example.thecodecup.data.local.model.Voucher
import com.example.thecodecup.data.local.model.staticVoucherList // <-- Import danh sách tĩnh
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf // <-- Import flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoucherRepository @Inject constructor(
    private val voucherDao: VoucherDao
) {

    fun getAvailableVouchers(): Flow<List<Voucher>> {
        return voucherDao.getAvailableVouchers()
    }

    suspend fun updateVoucher(voucher: Voucher) {
        voucherDao.updateVoucher(voucher)
    }

    suspend fun insertVouchers(voucher: Voucher) {
        voucherDao.insertVoucher(voucher)
    }


}