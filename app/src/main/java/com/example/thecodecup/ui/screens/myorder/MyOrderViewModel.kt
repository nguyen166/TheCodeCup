package com.example.thecodecup.ui.screens.myorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

data class MyOrdersUiState(
    val ongoingOrders: List<Order> = emptyList(),
    val historyOrders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Sử dụng `combine` để gộp 2 flow lại thành 1 UiState duy nhất
    val uiState: StateFlow<MyOrdersUiState> = combine(
        orderRepository.getOngoingOrders(),
        orderRepository.getHistoryOrders()
    ) { ongoing, history ->
        MyOrdersUiState(
            ongoingOrders = ongoing,
            historyOrders = history,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyOrdersUiState()
    )
}



//ham format de hien thi
fun formatOrderTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM | hh:mm a", Locale.ENGLISH)
    return sdf.format(Date(timestamp))
}