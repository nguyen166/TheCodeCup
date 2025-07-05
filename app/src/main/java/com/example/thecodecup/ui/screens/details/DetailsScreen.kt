package com.example.thecodecup.ui.screens.details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.OptionRow
import com.example.thecodecup.ui.components.QuantitySelector
import com.example.thecodecup.ui.components.SelectableButton
import com.example.thecodecup.ui.components.SelectableIconButton
import com.example.thecodecup.ui.screens.profile.ProfileViewModel
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

// Dữ liệu giả cho việc xây dựng UI
data class DetailsState(
    val name: String = "Americano",
    @DrawableRes val imageRes: Int = R.drawable.americano,
    val quantity: Int = 1,
    val shot: String = "Single", // "Single" or "Double"
    val type: String = "Cold",   // "Hot" or "Cold"
    val size: String = "L",      // "S", "M", "L"
    val ice: String = "Some",    // "None", "Some", "Full"
    val totalPrice: Double = 3.00,
)

@Composable
fun DetailsRoute(
    viewModel: DetailsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel=hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    var showProfileDialog by remember { mutableStateOf(false)}

        if (showProfileDialog) {
            AlertDialog(
                containerColor = AppTheme.colorScheme.surface,
                shape = AppTheme.shapes.medium,
                onDismissRequest = { showProfileDialog = false },
                title = {
                    Text(
                        text = "Update Profile",
                        style = AppTheme.typography.titleLarge,
                        color = AppTheme.colorScheme.onSurface
                    )
                },
                text = {
                    Text(
                        text = "Please update your address before adding items to the cart.",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.extendedColors.textGrey
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showProfileDialog = false
                            onNavigateToProfile()
                        },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppTheme.colorScheme.primary,
                            contentColor = AppTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Go to Profile")
                    }
                },


                dismissButton = {
                    TextButton(onClick = { showProfileDialog = false }) {
                        Text("Later", color = AppTheme.colorScheme.primary)
                    }
                }
            )
        }

        if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Chuyển đổi DetailsUiState thành DetailsState mà UI cần
        val detailsState = DetailsState(
            name = uiState.coffee?.name ?: "Unknown",
            imageRes = uiState.coffee?.imageRes ?: R.drawable.ic_size, //khi bi loi null khong co image res
            quantity = uiState.quantity,
            shot = uiState.selectedShot,
            type = uiState.selectedType,
            size = uiState.selectedSize,
            ice = if (uiState.selectedType == "Hot") "Not" else (uiState.selectedIce ?: "Some"),
            totalPrice = uiState.totalPrice
        )

        DetailsScreen(
            state = detailsState,
            onQuantityChange = viewModel::onQuantityChange,
            onShotChange = viewModel::onShotChange,
            onTypeChange = viewModel::onTypeChange,
            onSizeChange = viewModel::onSizeChange,
            onIceChange = viewModel::onIceChange,
            onAddToCart = {
                val address= profileUiState.userProfile?.address ?: ""
                if (address.isBlank() || address.equals("unknown", ignoreCase = true)) {
                    showProfileDialog = true
                } else {
                    viewModel.addToCart(userAddress=address,onCartSuccess = onNavigateToCart)
                }
            },
            onBackClick = onBackClick,
            onCartClick = onNavigateToCart
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    state: DetailsState,
    onQuantityChange: (Int) -> Unit,
    onShotChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onSizeChange: (String) -> Unit,
    onIceChange: (String) -> Unit,
    onAddToCart: () -> Unit,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.details_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back", tint = AppTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(painter = painterResource(id = R.drawable.ic_cart_checkout), contentDescription = "Cart", tint = AppTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.details_total_amount_label),
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.extendedColors.textMuted
                        )
                        Text(
                            text = "$${"%.2f".format(state.totalPrice)}",
                            style = AppTheme.typography.titleLarge.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.fillMaxWidth(0.65f),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        shape = AppTheme.shapes.large
                    ) {
                        Text(text = stringResource(id = R.string.details_add_to_cart_button))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = state.imageRes),
                contentDescription = state.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.0f)
                    .clip(AppTheme.shapes.large),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = state.name, style = AppTheme.typography.titleLarge)
                QuantitySelector(
                    quantity = state.quantity,
                    onIncrease = { onQuantityChange(state.quantity + 1) },
                    onDecrease = { onQuantityChange(state.quantity - 1) }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = AppTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            // --- CÁC DÒNG TÙY CHỌN ---

            OptionRow(title = stringResource(id = R.string.details_option_shot)) {
                SelectableButton(text = "Single", isSelected = state.shot == "Single", onClick = { onShotChange("Single") })
                SelectableButton(text = "Double", isSelected = state.shot == "Double", onClick = { onShotChange("Double") })
            }

            OptionRow(title = stringResource(id = R.string.details_option_select)) {
                SelectableIconButton(iconRes = R.drawable.ic_hot, contentDescription = "Hot", isSelected = state.type == "Hot", onClick = { onTypeChange("Hot") },)
                SelectableIconButton(iconRes = R.drawable.ic_cold, contentDescription = "Cold", isSelected = state.type == "Cold", onClick = { onTypeChange("Cold") })
            }

            OptionRow(title = stringResource(id = R.string.details_option_size)) {
                SelectableIconButton(iconRes = R.drawable.ic_size, contentDescription = "Size S", isSelected = state.size == "S", onClick = { onSizeChange("S") },iconModifier = Modifier.size(15.dp) )
                SelectableIconButton(iconRes = R.drawable.ic_size, contentDescription = "Size M", isSelected = state.size == "M", onClick = { onSizeChange("M")},iconModifier = Modifier.size(18.dp) )
                SelectableIconButton(iconRes = R.drawable.ic_size, contentDescription = "Size L", isSelected = state.size == "L", onClick = { onSizeChange("L")},iconModifier = Modifier.size(32.dp)  )
            }
            if(state.type=="Cold") {
                OptionRow(title = stringResource(id = R.string.details_option_ice)) {
                    SelectableIconButton(
                        iconRes = R.drawable.ic_ice1,
                        contentDescription = "No Ice",
                        isSelected = state.ice == "None",
                        onClick = { onIceChange("None") },
                        iconModifier = Modifier.size(16.dp)
                    )
                    SelectableIconButton(
                        iconRes = R.drawable.ic_ice2,
                        contentDescription = "Some Ice",
                        isSelected = state.ice == "Some",
                        onClick = { onIceChange("Some") },
                        iconModifier = Modifier.size(48.dp)
                    )
                    SelectableIconButton(
                        iconRes = R.drawable.ic_ice3,
                        contentDescription = "Full Ice",
                        isSelected = state.ice == "Full",
                        onClick = { onIceChange("Full") },
                        iconModifier = Modifier.size(72.dp)
                    )
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailsScreenPreview() {
    var state by remember { mutableStateOf(DetailsState()) }
    TheCodeCupTheme {
        DetailsScreen(
            state = state,
            onQuantityChange = { newState -> state = state.copy(quantity = newState) },
            onShotChange = { newShot -> state = state.copy(shot = newShot) },
            onTypeChange = { newType -> state = state.copy(type = newType) },
            onSizeChange = { newSize -> state = state.copy(size = newSize) },
            onIceChange = { newIce -> state = state.copy(ice = newIce) },
            onAddToCart = {},
            onBackClick = {},
            onCartClick = {}
        )
    }
}