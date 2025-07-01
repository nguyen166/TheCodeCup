package com.example.thecodecup.ui.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.thecodecup.data.local.model.Coffee
import com.example.thecodecup.data.local.model.staticCoffeeList

// Data class đại diện cho toàn bộ trạng thái của HomeScreen
data class HomeUiState(
    val userName: String = "Anderson", // Tạm thời hard-code
    val stamps: Int = 0,
    val coffeeList: List<Coffee> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    // Sẽ inject CoffeeRepository sau nếu cần
) : ViewModel() {

    // Danh sách cà phê, tạm thời hard-code ở đây.
    // Trong app thật, nó sẽ đến từ một Repository.(vi chưa cần code backend )
    private val _coffeeList = staticCoffeeList
    val uiState: StateFlow<HomeUiState> =
        settingsDataStore.loyaltyStampsFlow.map { stamps ->
            HomeUiState(stamps = stamps, coffeeList = _coffeeList)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(coffeeList = _coffeeList)
        )

}