package com.example.thecodecup.ui.screens.redeem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.R
import com.example.thecodecup.data.datastore.SettingsDataStore
import com.example.thecodecup.data.local.model.Voucher
import com.example.thecodecup.data.repository.VoucherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Trạng thái của màn hình Redeem
data class RedeemUiState(
    val items: List<RedeemableItem> = emptyList()
)

enum class RedeemType { DRINK, VOUCHER }

// Dữ liệu giả cho các vật phẩm
// Có thể đặt trong một file model riêng nếu muốn
private val staticRedeemableItems = listOf(
    RedeemableItem(1, RedeemType.DRINK, "Cafe Latte", imageRes = R.drawable.americano, pointsCost = 1340),
    RedeemableItem(2,RedeemType.DRINK ,"Flat White", imageRes = R.drawable.flatwhite, pointsCost = 1340),
    RedeemableItem(3, RedeemType.DRINK,"Cappuccino", imageRes = R.drawable.capuchino, pointsCost = 1340),
    RedeemableItem(4, RedeemType.VOUCHER, "15% OFF Voucher", description = "For your next order", imageRes = R.drawable.ic_voucher, pointsCost = 1000),
    RedeemableItem(5, RedeemType.VOUCHER, "$1 OFF OFF Voucher", description = "For your next order", imageRes = R.drawable.ic_voucher, pointsCost = 1200)
)

@HiltViewModel
class RedeemViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val voucherRepository: VoucherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Tải danh sách vật phẩm (hiện tại là từ dữ liệu tĩnh)
        _uiState.update { it.copy(items = staticRedeemableItems) }
    }

    /**
     * Xử lý logic khi người dùng nhấn đổi điểm cho một vật phẩm.
     * @param itemId ID của vật phẩm muốn đổi.
     * @param onResult Callback được gọi với kết quả: true nếu thành công, false nếu thất bại.
     */
    fun redeemItem(itemId: Int, onResult: (isSuccess: Boolean, message: String) -> Unit) {
        viewModelScope.launch {
            val itemToRedeem = _uiState.value.items.find { it.id == itemId } ?: return@launch
            val currentUserPoints = settingsDataStore.rewardPointsFlow.first()

            if (currentUserPoints < itemToRedeem.pointsCost) {
                onResult(false, "Not enough points!")
                return@launch
            }

            // Trừ điểm của người dùng
            settingsDataStore.redeemPoints(itemToRedeem.pointsCost)

            // Xử lý logic dựa trên loại vật phẩm
            when (itemToRedeem.type) {
                RedeemType.DRINK -> {
                    println("Redeemed drink: ${itemToRedeem.name}")
                    onResult(true, "Redeemed ${itemToRedeem.name} successfully!")
                }
                RedeemType.VOUCHER -> {
                    val newVoucher = Voucher(
                        title = itemToRedeem.name,
                        description = itemToRedeem.description ?: "Enjoy your discount!",
                        discountType = "PERCENTAGE", // Hoặc dựa trên thông tin từ itemToRedeem
                        discountValue = 15.0,
                        minOrderValue = 0.0
                    )
                    voucherRepository.insertVoucher(newVoucher)
                    onResult(true, "Voucher added to your wallet!")
                }
            }
        }
    }
}