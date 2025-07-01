package com.example.thecodecup.ui.screens.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.datastore.SettingsDataStore
import com.example.thecodecup.data.repository.OrderRepository
import com.example.thecodecup.ui.screens.myorder.formatOrderTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


data class RewardHistoryItem(
    val id: Int,
    val firstItemName: String,
    val date: String, // Cần hàm để format timestamp
    val pointsEarned: Int
)


data class RewardsUiState(
    val stamps: Int = 0,
    val points: Int = 0,
    val history: List<RewardHistoryItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val uiState: StateFlow<RewardsUiState> = combine(
        settingsDataStore.loyaltyStampsFlow,
        settingsDataStore.rewardPointsFlow,
        orderRepository.getHistoryOrders(),
        orderRepository.getOngoingOrders(),
    ) { stamps, points, historyOrders, ongoingOrders ->

        val allOrders = historyOrders + ongoingOrders

        // Chuyển đổi List<Order> thành List<RewardHistoryItem>
        val rewardHistory = allOrders.map { order ->
            RewardHistoryItem(
                id = order.id,
                firstItemName = order.items.firstOrNull()?.name ?: "Order",
                date = formatOrderTimestamp(order.timestamp), // Cần tạo hàm này
                pointsEarned = order.pointsAwarded
            )
        }

        RewardsUiState(
            stamps = stamps,
            points = points,
            history = rewardHistory,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RewardsUiState() // Trạng thái ban đầu là đang tải
    )
}

