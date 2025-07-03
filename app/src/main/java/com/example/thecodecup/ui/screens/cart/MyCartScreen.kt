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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.thecodecup.data.local.model.Voucher
import androidx.compose.runtime.setValue
import com.example.thecodecup.ui.components.VoucherCard
import com.example.thecodecup.ui.screens.profile.ProfileViewModel

private val previewCartItems = listOf(
    CartItem(1, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.americano,"unknown"),
    CartItem(2, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.capuchino,"unknown"),
    CartItem(3, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.flatwhite,"unknown")
)
private val previewTotalPrice = 9.0

@Composable
fun CartRoute(
    viewModel: CartViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToOrderSuccess: () -> Unit
) {
    // Lấy state thật từ ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val address = uiState.cartItems.firstOrNull()?.address ?: ""



    // Gọi Composable UI (stateless) với dữ liệu và sự kiện thật
    CartScreen(
        uiState=uiState,
        onBackClick = onBackClick,
        onCheckoutClick = {
            viewModel.onCheckoutClicked(userAddress =address,onCheckoutSuccess = onNavigateToOrderSuccess)
        },
        onRemoveItem = { itemToRemove ->
            viewModel.removeItem(
                userAddress =  itemToRemove.address,
                itemToRemove = itemToRemove,
                )
        },
        onApplyVoucher = viewModel::applyVoucher,
        onRemoveVoucherClick = viewModel::removeVoucher
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    uiState: CartUiState,
    onBackClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onApplyVoucher: (Voucher) -> Unit,
    onRemoveVoucherClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var isVoucherSheetOpen by rememberSaveable { mutableStateOf(false) }


    if (isVoucherSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isVoucherSheetOpen = false },
            sheetState = sheetState,
            containerColor = AppTheme.colorScheme.surface
        ) {
            // Thêm một chút padding cho tiêu đề
            Text(
                text = "Apply a Voucher",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.availableVouchers.isEmpty()) {
                    item {
                        Text(
                            text = "You have no available vouchers.",
                            modifier = Modifier.padding(vertical = 24.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(uiState.availableVouchers) { voucher ->
                        VoucherCard(voucher = voucher, onClick = {
                            onApplyVoucher(voucher)
                            // Tự động đóng bottom sheet sau khi chọn
                            isVoucherSheetOpen = false
                        })
                    }
                }
            }
        }
    }
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
                subtotal = uiState.subtotal,
                discount = uiState.discountAmount,
                totalPrice = uiState.totalPrice,
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
            items(uiState.cartItems, key = { it.id }) { item ->


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
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = AppTheme.colorScheme.outline.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.appliedVoucher == null) {
                    OutlinedButton(
                        onClick = { isVoucherSheetOpen = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Apply Voucher")
                    }
                } else {
                    AppliedVoucherInfo(
                        voucher = uiState.appliedVoucher,
                        onRemoveClick = onRemoveVoucherClick
                    )
                }
            }
        }
    }
}


@Composable
fun CartBottomBar(
    subtotal: Double,
    discount: Double,
    totalPrice: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dòng Subtotal
            PriceRow(label = "Subtotal", amount = subtotal)

            // Dòng Discount (chỉ hiển thị nếu có giảm giá)
            if (discount > 0) {
                PriceRow(label = "Discount", amount = -discount, color = AppTheme.colorScheme.primary)
            }

            // Dòng tổng tiền cuối cùng
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.cart_total_price_label),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.extendedColors.textGrey
                )
                Text(
                    text = formatCurrency(totalPrice),
                    style = AppTheme.typography.headlineSmall, // Dùng size nhỏ hơn một chút
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nút Checkout
            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = AppTheme.shapes.large
            ) {
                Icon(painterResource(id = R.drawable.ic_cart_checkout), null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(id = R.string.cart_checkout_button))
            }
        }
    }
}

// Composable tái sử dụng cho các dòng giá
@Composable
private fun PriceRow(label: String, amount: Double, color: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = color)
        Text(text = formatCurrency(amount), style = MaterialTheme.typography.bodyLarge, color = color)
    }
}

// Hàm tiện ích để format tiền tệ
private fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    format.maximumFractionDigits = 2
    return format.format(price)
}

@Composable
fun AppliedVoucherInfo(voucher: Voucher, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Applied Voucher",
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colorScheme.primary
            )
            Text(
                text = voucher.title,
                style = AppTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = onRemoveClick) {
            Icon(Icons.Default.Close, contentDescription = "Remove Voucher")
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun CartScreenPreview() {
//    TheCodeCupTheme {
//        CartScreen(
//            cartItems = previewCartItems,
//            totalPrice = previewTotalPrice,
//            onBackClick = { },
//            onCheckoutClick = { },
//            onRemoveItem = { }
//        )
//    }
//}