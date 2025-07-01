package com.example.thecodecup.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.local.model.CartItem
import com.example.thecodecup.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.thecodecup.data.repository.OrderRepository
import com.example.thecodecup.data.datastore.SettingsDataStore
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.data.local.model.OrderItem
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString


// trạng thái (State) của màn hình.
data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // 1. TẠO STATE BÊN TRONG VIEWMODEL
    // Dùng MutableStateFlow để giữ trạng thái có thể thay đổi.
    private val _uiState = MutableStateFlow(CartUiState())

    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    // 2. LẤY DỮ LIỆU KHI VIEWMODEL ĐƯỢC KHỞI TẠO
    init {
        // Khởi chạy một coroutine trong `viewModelScope`.
        // Coroutine này sẽ tự động bị hủy khi ViewModel bị hủy, tránh rò rỉ bộ nhớ.
        viewModelScope.launch {
            // `getCartItems()` từ Repository trả về một Flow.
            cartRepository.getCartItems().collect { items ->
                // Mỗi khi dữ liệu trong bảng 'cart_items' của Room thay đổi,
                // Flow sẽ phát ra một danh sách `items` mới và khối code này sẽ chạy.
                _uiState.update { currentState ->
                    // `update` là cách an toàn để thay đổi giá trị của MutableStateFlow.
                    currentState.copy(
                        cartItems = items,
                        // Tính tổng giá tiền mỗi khi danh sách cập nhật.
                        totalPrice = items.sumOf { it.price }
                    )
                }
            }
        }
    }

    fun removeItem(userAddress: String,itemToRemove: CartItem) {
        viewModelScope.launch {
            // 1. Chuyển đổi CartItem thành một Order đã bị hủy
            val cancelledOrder = convertCartItemToCancelledOrder(userAddress,itemToRemove)

            // 2. Lưu đơn hàng đã hủy này vào lịch sử
            orderRepository.insertOrder(cancelledOrder)

            // 3. Xóa item khỏi giỏ hàng hiện tại
            cartRepository.removeFromCart(itemToRemove)
        }
    }

    private fun convertCartItemToCancelledOrder(userAddress: String,cartItem: CartItem): Order {
        // Chuyển đổi thành một OrderItem duy nhất
        val orderItem = OrderItem(
            coffeeId = cartItem.coffeeId,
            name = cartItem.name,
            quantity = cartItem.quantity,
            size = cartItem.size,
            shot = cartItem.shot,
            ice = cartItem.ice,
            pricePerItem = cartItem.price / cartItem.quantity
        )

        // Chuyển danh sách (chỉ có 1 item) thành chuỗi JSON
        val itemsJsonString = Json.encodeToString(listOf(orderItem))

        return Order(
            // id sẽ tự động được tạo
            totalPrice = cartItem.price,
            timestamp = System.currentTimeMillis(),
            status = "history",
            itemsJson = itemsJsonString,
            pointsAwarded = 0, // Không có điểm cho đơn hàng bị hủy,
            address = if (userAddress.isNotBlank()) userAddress else "N/A"
        )
    }

    fun onCheckoutClicked(userAddress: String,onCheckoutSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentCartItems = _uiState.value.cartItems
            if (currentCartItems.isEmpty()) return@launch


            val orderItems = currentCartItems.map { cartItem ->
                OrderItem(
                    coffeeId = cartItem.coffeeId,
                    name = cartItem.name,
                    quantity = cartItem.quantity,
                    size = cartItem.size,
                    shot = cartItem.shot,
                    ice = cartItem.ice,
                    pricePerItem = cartItem.price / cartItem.quantity
                )
            }
            val itemsJsonString = Json.encodeToString<List<OrderItem>>(orderItems)

            // 2. Tính toán điểm thưởng (ví dụ: 10 điểm cho mỗi đô la)
            val totalPrice = _uiState.value.totalPrice
            val pointsToAward = (totalPrice * 10).toInt()

            // 3. Tạo đối tượng Order mới
            val newOrder = Order(
                totalPrice = totalPrice,
                timestamp = System.currentTimeMillis(),
                status = "ongoing",
                itemsJson = itemsJsonString,
                pointsAwarded = pointsToAward,
                address = if (userAddress.isNotBlank()) userAddress else "N/A"
            )


            orderRepository.insertOrder(newOrder)
            settingsDataStore.addRewardPoints(pointsToAward)
            settingsDataStore.incrementLoyaltyStamp()
            if(settingsDataStore.getCurrentLoyaltyStamps()==8)
                settingsDataStore.resetLoyaltyStamps()
            cartRepository.clearCart() // Xóa giỏ hàng sau khi đã tạo đơn hàng


            onCheckoutSuccess()
        }
    }
}