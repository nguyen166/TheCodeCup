package com.example.thecodecup.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.local.model.CartItem
import com.example.thecodecup.data.repository.CartRepository
import com.example.thecodecup.data.local.model.Coffee
import com.example.thecodecup.data.local.model.staticCoffeeList
import com.example.thecodecup.ui.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


// **QUAN TRỌNG**: Đổi tên data class này thành DetailsUiState
// để phân biệt với State của Compose. Đây là state của UI Layer.
data class DetailsUiState(
    val coffee: Coffee? = null,
    val quantity: Int = 1,
    val selectedShot: String = "Single",
    val selectedType: String = "Cold",
    val selectedSize: String = "M",
    val selectedIce: String = "Some",
    val isLoading: Boolean = true
) {
    // Thêm một computed property để tính giá
    val totalPrice: Double
        get() {
            if (coffee == null) return 0.0
            // Logic tính giá phức tạp hơn có thể thêm ở đây
            // Ví dụ: size L +$0.5, double shot +$0.5
            var finalPrice = coffee.basePrice
            if (selectedSize == "L") finalPrice += 0.3 else if (selectedSize == "S") finalPrice -= 0.5
            if (selectedShot == "Double") finalPrice += 0.5
            return finalPrice * quantity
        }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val coffeeId: Int = checkNotNull(savedStateHandle[AppDestinations.DETAILS_ID_ARG])

    init {
        loadCoffeeDetails(coffeeId)
    }

    private fun loadCoffeeDetails(id: Int) {
        val coffee = staticCoffeeList.find { it.id == id }
        _uiState.update {
            it.copy(
                coffee = coffee,
                isLoading = false
            )
        }
    }

    // --- Các hàm xử lý sự kiện từ UI ---

    fun onQuantityChange(newQuantity: Int) {
        if (newQuantity >= 1) {
            _uiState.update { it.copy(quantity = newQuantity) }
        }
    }

    fun onShotChange(newShot: String) {
        _uiState.update { it.copy(selectedShot = newShot) }
    }

    fun onTypeChange(newType: String) {
        _uiState.update { it.copy(selectedType = newType) }
    }

    fun onSizeChange(newSize: String) {
        _uiState.update { it.copy(selectedSize = newSize) }
    }

    fun onIceChange(newIce: String) {
        _uiState.update { it.copy(selectedIce = newIce) }
    }

    fun addToCart(userAddress:String,onCartSuccess: () -> Unit) {
        val currentState = _uiState.value
        val coffee = currentState.coffee ?: return

        val cartItem = CartItem(
            coffeeId = coffee.id,
            name = coffee.name,
            price = currentState.totalPrice,
            quantity = currentState.quantity,
            size = currentState.selectedSize,
            shot = currentState.selectedShot,
            ice = currentState.selectedIce,
            imageResId = coffee.imageRes,
            address = userAddress
        )

        viewModelScope.launch {
            cartRepository.addToCart(cartItem)
            // Sau khi thêm thành công, gọi callback để điều hướng
            onCartSuccess()
        }
    }
}