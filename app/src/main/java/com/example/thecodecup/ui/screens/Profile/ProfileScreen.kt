package com.example.thecodecup.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.UserProfile
import com.example.thecodecup.ui.components.EditProfileDialog
import com.example.thecodecup.ui.components.ProfileInfoRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var editingField by rememberSaveable { mutableStateOf<String?>(null) }

    when {
        // 1. Nếu đang loading (lần đầu vào)
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // 2. Nếu loading xong và không có profile
        uiState.userProfile == null -> {
            // Đây là một Side Effect. Nó chỉ nên chạy MỘT LẦN khi điều kiện này đúng.
            LaunchedEffect(Unit) {
                viewModel.createDefaultProfile()
            }

            // Trong khi chờ profile được tạo, vẫn hiển thị loading
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            val currentProfile = uiState.userProfile!!// An toàn vì đã kiểm tra null
            // Cập nhật các state của TextField khi profile thay đổi
            var name by remember(currentProfile.fullName) { mutableStateOf(currentProfile.fullName) }

            // Khi người dùng nhấn lưu trong dialog
            val handleSave = { updatedValue: String ->
                // Sử dụng bản sao cục bộ ở đây
                if (currentProfile != null) {
                    val newProfile = when (editingField) {
                        "full_name" -> currentProfile.copy(fullName = updatedValue)
                        "phone_number" -> currentProfile.copy(phoneNumber = updatedValue)
                        "email" -> currentProfile.copy(email = updatedValue)
                        "address" -> currentProfile.copy(address = updatedValue)
                        else -> currentProfile
                    }
                    viewModel.saveProfile(newProfile)
                }
                editingField = null
            }


            // Hiển thị dialog nếu có một trường đang được edit
            // Sử dụng bản sao cục bộ ở đây
            if (editingField != null && currentProfile != null) {
                val (label, initialValue) = when (editingField) {
                    "full_name" -> "Full name" to currentProfile.fullName
                    "phone_number" -> "Phone number" to currentProfile.phoneNumber
                    "email" -> "Email" to currentProfile.email
                    "address" -> "Address" to currentProfile.address
                    else -> "" to ""
                }

                EditProfileDialog(
                    label = label,
                    initialValue = initialValue,
                    onDismiss = { editingField = null },
                    onConfirm = handleSave
                )
            }

            // Và sử dụng bản sao cục bộ ở đây để quyết định hiển thị
            if (currentProfile != null) {
                ProfileScreen(
                    userProfile = currentProfile, // Truyền bản sao bất biến xuống
                    onBackClick = onBackClick,
                    onEditClick = { fieldKey -> editingField = fieldKey }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit // Truyền một field-key để biết sửa cái gì
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.profile_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painterResource(id = R.drawable.ic_arrow_back), "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_full_name),
                value = userProfile.fullName,
                icon = painterResource(id = R.drawable.ic_profile),
                onEditClick = { onEditClick("full_name") }
            )
            Divider()
            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_phone_number),
                value = userProfile.phoneNumber,
                icon = painterResource(id = R.drawable.ic_contact_phone),
                onEditClick = { onEditClick("phone_number") }
            )
            Divider()
            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_email),
                value = userProfile.email,
                icon = painterResource(id = R.drawable.ic_message),
                onEditClick = { onEditClick("email") }
            )
            Divider()
            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_address),
                value = userProfile.address,
                icon = painterResource(id = R.drawable.ic_location_1),
                onEditClick = { onEditClick("address") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TheCodeCupTheme {
        val mockUser = UserProfile(
            id = 1,
            fullName = "Trọng Nguyễn",
            phoneNumber = "0123 456 789",
            email = "trong.nguyen@example.com",
            address = "123 Đường Lý Thường Kiệt, Quận 10, TP.HCM"
        )

        ProfileScreen(
            userProfile = mockUser,
            onBackClick = {},
            onEditClick = {}
        )
    }
}


