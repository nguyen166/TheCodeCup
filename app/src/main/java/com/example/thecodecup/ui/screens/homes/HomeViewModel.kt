package com.example.thecodecup.ui.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.R
import com.example.thecodecup.data.datastore.SettingsDataStore
import com.example.thecodecup.data.local.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.thecodecup.data.local.model.Coffee
import com.example.thecodecup.data.local.model.staticCoffeeList
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.data.repository.CartRepository
import com.example.thecodecup.data.repository.OrderRepository
import com.example.thecodecup.data.repository.ProfileRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Data class đại diện cho toàn bộ trạng thái của HomeScreen
data class HomeUiState(
    val userName: String = "", // Tạm thời hard-code
    val stamps: Int = 0,
    val coffeeList: List<Coffee> = emptyList(),
    val latestOrder: Order? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    fun reorderFromOrder(
        order: Order, // Nhận vào một đối tượng Order
        userAddress: String,
        onCartSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // Lấy danh sách items từ thuộc tính @Ignore
            val orderItems = order.items

            val cartItems = orderItems.map { orderItem ->
                // Logic chuyển đổi từ OrderItem sang CartItem
                // Lưu ý: Cần đảm bảo có đủ thông tin
                CartItem(
                    coffeeId = orderItem.coffeeId,
                    name = orderItem.name,
                    type = orderItem.type,
                    price = orderItem.pricePerItem*orderItem.quantity,
                    quantity = orderItem.quantity,
                    size = orderItem.size,
                    shot = orderItem.shot,
                    ice = orderItem.ice,
                    imageResId = findImageResForCoffee(orderItem.coffeeId),
                    address = userAddress
                )
            }

            cartRepository.addAllToCart(cartItems)
            onCartSuccess()
        }
    }

    // Hàm helper để tìm imageRes, bạn cần implement nó
    private fun findImageResForCoffee(coffeeId: Int): Int {
        // Logic tra cứu từ staticCoffeeList hoặc CoffeeRepository
        return staticCoffeeList.find { it.id == coffeeId }?.imageRes ?: R.drawable.ic_size
    }

    // Danh sách cà phê, tạm thời hard-code ở đây.
    // Trong app thật, nó sẽ đến từ một Repository.(vi chưa cần code backend )
    private val _coffeeList = staticCoffeeList
    val uiState: StateFlow<HomeUiState> =
        combine(
            settingsDataStore.loyaltyStampsFlow,
            orderRepository.getLatestOrder(),
            profileRepository.getUserProfile()
        ) { stamps, latestOrder, userProfile ->
            // Bây giờ, bạn tạo HomeUiState với đầy đủ thông tin
            HomeUiState(
                userName = userProfile?.fullName ?: "Guest", // <-- Dùng tên từ profile
                stamps = stamps,
                latestOrder = latestOrder,
                coffeeList = staticCoffeeList
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState() // Initial value vẫn không có tên
        )

}