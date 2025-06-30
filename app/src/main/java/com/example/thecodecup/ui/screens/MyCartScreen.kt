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
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.CartItem
import com.example.thecodecup.ui.components.CartItemRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import java.text.NumberFormat
import java.util.*


private val previewCartItems = listOf(
    CartItem(1, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.americano),
    CartItem(1, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.capuchino),
    CartItem(1, 1, "Cappuccino", 3.0, 1, "medium", "single", "full ice", R.drawable.flatwhite)
)
private val previewTotalPrice = 9.0

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
            items(cartItems) {
                CartItemRow(
                    item=it
                )
            }
        }
    }
}

@Composable
private fun CartBottomBar(
    totalPrice: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp // Thêm đổ bóng để phân biệt với nền
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