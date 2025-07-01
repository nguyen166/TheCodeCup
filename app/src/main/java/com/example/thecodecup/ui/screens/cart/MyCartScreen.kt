package com.example.thecodecup.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.CartItem
import com.example.thecodecup.ui.components.CartItemRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import java.text.NumberFormat
import java.util.*
import androidx.compose.runtime.getValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.rememberSwipeToDismissBoxState
import com.example.thecodecup.ui.screens.profile.ProfileViewModel

private val previewCartItems = listOf(
    CartItem(1, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.americano),
    CartItem(2, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.capuchino),
    CartItem(3, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.flatwhite)
)
private val previewTotalPrice = 9.0

@Composable
fun CartRoute(
    viewModel: CartViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToOrderSuccess: () -> Unit
) {
    // Lấy state thật từ ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    val address= profileUiState.userProfile?.address ?: ""

    // Gọi Composable UI (stateless) với dữ liệu và sự kiện thật
    CartScreen(
        cartItems = uiState.cartItems,
        totalPrice = uiState.totalPrice,
        onBackClick = onBackClick,
        onCheckoutClick = {

            viewModel.onCheckoutClicked(userAddress =address,onCheckoutSuccess = onNavigateToOrderSuccess)
        },
        onRemoveItem = { itemToRemove ->
            viewModel.removeItem(
                itemToRemove = itemToRemove,
                userAddress =  address
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onRemoveItem: (CartItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.cart_screen_title),
                        style = AppTheme.typography.titleLarge
                    )
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
        },
        bottomBar = {
            CartBottomBar(
                totalPrice = totalPrice,
                onCheckoutClick = onCheckoutClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp)
        ) {
            items(cartItems, key = { it.id }) { item ->


                val dismissState = rememberSwipeToDismissBoxState(
                    // `confirmValueChange` vẫn hoạt động tương tự
                    confirmValueChange = { dismissValue ->
                        // Chỉ xử lý khi item bị vuốt đi hoàn toàn
                        if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            onRemoveItem(item)
                            true // Xác nhận thay đổi, item sẽ bị xóa
                        } else {
                            false // Không làm gì nếu vuốt chưa đủ
                        }
                    },
                   positionalThreshold = { it * 0.25f }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    // Chỉ cho phép vuốt từ phải sang trái
                    enableDismissFromStartToEnd = false,
                    enableDismissFromEndToStart = true,

                    // Giao diện nền (background) được định nghĩa theo một cách khác
                    backgroundContent = {
                        val color by animateColorAsState(
                            targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                Color.Transparent,
                            label = "background color"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize().background(color, shape = AppTheme.shapes.large)
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                ) {
                    CartItemRow(item = item)
                }
            }
        }
    }
}

@Composable
fun CartBottomBar(
    totalPrice: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.cart_total_price_label),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.extendedColors.textMuted
                )
                Text(
                    text = formatCurrency(totalPrice),
                    style = AppTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.onBackground
                )
            }
            Button(
                onClick = onCheckoutClick,
                shape = AppTheme.shapes.large,
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart_checkout),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(id = R.string.cart_checkout_button))
            }
        }
    }
}

// Hàm tiện ích để format tiền tệ
private fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    format.maximumFractionDigits = 2
    return format.format(price)
}

@Preview(showSystemUi = true)
@Composable
fun CartScreenPreview() {
    TheCodeCupTheme {
        CartScreen(
            cartItems = previewCartItems,
            totalPrice = previewTotalPrice,
            onBackClick = { },
            onCheckoutClick = { },
            onRemoveItem = { }
        )
    }
}