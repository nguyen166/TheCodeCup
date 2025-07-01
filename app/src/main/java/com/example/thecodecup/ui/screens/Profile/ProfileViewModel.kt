package com.example.thecodecup.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.local.model.UserProfile
import com.example.thecodecup.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Thêm một state để biết có đang loading hay không
    private val _isLoading = MutableStateFlow(true)

    // Kết hợp state loading và user profile
    val uiState: StateFlow<ProfileUiState> = profileRepository.getUserProfile()
        .map { profile ->
            ProfileUiState(userProfile = profile, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState(isLoading = true)
        )

    //nhận một user đã được cập nhật
    fun saveProfile(updatedProfile: UserProfile) {
        viewModelScope.launch {
            profileRepository.updateUserProfile(updatedProfile)
        }
    }

    fun createDefaultProfile() {
        viewModelScope.launch {
            // Kiểm tra lại một lần nữa để chắc chắn profile vẫn là null
            if (uiState.value.userProfile == null) {
                val defaultProfile = UserProfile(
                    id = 1,
                    fullName = "Unknown",
                    phoneNumber = "Unknown",
                    email = "Unknown",
                    address = "Unknown"
                )
                // Gọi Repository để lưu
                profileRepository.updateUserProfile(defaultProfile)
            }
        }
    }
}

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = true
)