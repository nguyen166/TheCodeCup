package com.example.thecodecup.ui.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.thecodecup.data.local.model.Coffee
import com.example.thecodecup.data.local.model.staticCoffeeList
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.data.repository.OrderRepository
import kotlinx.coroutines.flow.combine

// Data class đại diện cho toàn bộ trạng thái của HomeScreen
data class HomeUiState(
    val userName: String = "Anderson", // Tạm thời hard-code
    val stamps: Int = 0,
    val coffeeList: List<Coffee> = emptyList(),
    val latestOrder: Order? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Danh sách cà phê, tạm thời hard-code ở đây.
    // Trong app thật, nó sẽ đến từ một Repository.(vi chưa cần code backend )
    private val _coffeeList = staticCoffeeList
    val uiState: StateFlow<HomeUiState> =

        combine(
            settingsDataStore.loyaltyStampsFlow,
            orderRepository.getLatestOrder()
        ) { stamps, latestOrder ->

            HomeUiState(
                stamps = stamps,
                latestOrder = latestOrder,
                coffeeList = staticCoffeeList
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

}