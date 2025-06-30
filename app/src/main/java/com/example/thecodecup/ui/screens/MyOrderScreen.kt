package com.example.thecodecup.ui.screens.myorder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.ui.components.OrderRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

// Dữ liệu giả
private val previewOngoingOrders = listOf(
    Order(1, 6.0, System.currentTimeMillis(), "ongoing", "[{\"name\":\"Americano\"},{\"name\":\"Cafe Latte\"}]", 12),
    Order(2, 3.0, System.currentTimeMillis() - 100000, "ongoing", "[{\"name\":\"Flat White\"}]", 12)
)
private val previewHistoryOrders = listOf(
    Order(3, 3.0, System.currentTimeMillis() - 200000, "history", "[{\"name\":\"Cappuccino\"}]", 12),
    Order(4, 3.0, System.currentTimeMillis() - 300000, "history", "[{\"name\":\"Mocha\"}]", 0) // Giả sử đơn hủy không có điểm
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    ongoingOrders: List<Order>,
    historyOrders: List<Order>,
    // Thêm các callback event sau này
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.my_orders_tab_ongoing),
        stringResource(id = R.string.my_orders_tab_history)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.my_orders_screen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            // Hiển thị danh sách dựa trên tab được chọn
            val ordersToShow = if (selectedTabIndex == 0) ongoingOrders else historyOrders

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(ordersToShow, key = { it.id }) { order ->
                    // Logic giả để lấy tên item từ JSON
                    // Ở GĐ 3, logic này sẽ nằm trong ViewModel
                    val itemNames = listOf("Item from order ${order.id}") // Placeholder

                    OrderRow(
                        itemNames = itemNames,
                        date = "24 June | 12:30 PM", // Placeholder
                        address = "3 Addersion Court Chino Hills...", // Placeholder
                        price = order.totalPrice
                    )
                    Divider(color = AppTheme.colorScheme.outline)
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MyOrderScreenPreview() {
    TheCodeCupTheme {
        MyOrderScreen(
            ongoingOrders = previewOngoingOrders,
            historyOrders = previewHistoryOrders
        )
    }
}