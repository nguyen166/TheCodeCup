package com.example.thecodecup.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.UserProfile
import com.example.thecodecup.ui.components.ProfileInfoRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

// Dữ liệu giả cho việc xây dựng UI
private val previewUserProfile = UserProfile(
    fullName = "Anderson",
    phoneNumber = "+60134589525",
    email = "Anderson@email.com",
    address = "3 Addersion Court\nChino Hills, HO56824, United State"
)


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
                title = {
                    Text(text = stringResource(id = R.string.profile_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(id = R.string.cd_back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_full_name),
                value = userProfile.fullName,
                icon = painterResource(id = R.drawable.ic_profile),
                onEditClick = { onEditClick("full_name") }
            )
            Divider(color = AppTheme.colorScheme.outline)

            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_phone_number),
                value = userProfile.phoneNumber,
                icon = painterResource(id = R.drawable.ic_contact_phone),
                onEditClick = { onEditClick("phone_number") }
            )
            Divider(color = AppTheme.colorScheme.outline)

            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_email),
                value = userProfile.email,
                icon = painterResource(id = R.drawable.ic_message),
                onEditClick = { onEditClick("email") }
            )
            Divider(color = AppTheme.colorScheme.outline)

            ProfileInfoRow(
                label = stringResource(id = R.string.profile_label_address),
                value = userProfile.address,
                icon = painterResource(id = R.drawable.ic_location_1),
                onEditClick = { onEditClick("address") }
            )
            Divider(color = AppTheme.colorScheme.outline)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    TheCodeCupTheme {
        ProfileScreen(
            userProfile = previewUserProfile,
            onBackClick = {},
            onEditClick = {}
        )
    }
}