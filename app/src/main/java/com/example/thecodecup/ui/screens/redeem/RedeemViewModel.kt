package com.example.thecodecup.ui.screens.redeem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.R
import com.example.thecodecup.data.datastore.SettingsDataStore
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

// Dữ liệu giả cho các vật phẩm
// Có thể đặt trong một file model riêng nếu muốn
private val staticRedeemableItems = listOf(
    RedeemableItem(1, "Cafe Latte", R.drawable.americano, "Valid until 04.07.24", 1340),
    RedeemableItem(2, "Flat White", R.drawable.flatwhite, "Valid until 04.07.24", 1340),
    RedeemableItem(3, "Cappuccino", R.drawable.capuchino, "Valid until 04.07.24", 1340)
)

@HiltViewModel
class RedeemViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
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
    fun redeemItem(itemId: Int, onResult: (isSuccess: Boolean) -> Unit) {
        viewModelScope.launch {
            val itemToRedeem = _uiState.value.items.find { it.id == itemId } ?: return@launch

            // Lấy số điểm hiện tại của người dùng
            val currentUserPoints = settingsDataStore.rewardPointsFlow.first()

            if (currentUserPoints >= itemToRedeem.pointsCost) {
                // Đủ điểm -> Trừ điểm và báo thành công
                settingsDataStore.redeemPoints(itemToRedeem.pointsCost)
                onResult(true)
            } else {
                // Không đủ điểm -> Báo thất bại
                onResult(false)
            }
        }
    }
}