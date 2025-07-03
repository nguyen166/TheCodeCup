package com.example.thecodecup.ui.screens.vouchers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.local.model.Voucher
import com.example.thecodecup.data.local.model.staticVoucherList
import com.example.thecodecup.data.repository.VoucherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class VouchersUiState(
    val availableVouchers: List<Voucher> =emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class VouchersViewModel @Inject constructor(
    voucherRepository: VoucherRepository
) : ViewModel() {

    val uiState: StateFlow<VouchersUiState> = voucherRepository.getAvailableVouchers()
        .map { vouchers ->
            VouchersUiState(availableVouchers = vouchers, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VouchersUiState()
        )
}