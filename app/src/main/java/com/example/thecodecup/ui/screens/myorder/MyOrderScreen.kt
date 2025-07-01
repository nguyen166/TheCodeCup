package com.example.thecodecup.ui.screens.myorder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.ui.components.AppBottomBar
import com.example.thecodecup.ui.components.OrderRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme


@Composable
fun MyOrderRoute(
    viewModel: MyOrdersViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyOrderScreen(
        ongoingOrders = uiState.ongoingOrders,
        historyOrders = uiState.historyOrders,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    ongoingOrders: List<Order>,
    historyOrders: List<Order>,
    navController: NavController
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
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
        },
        bottomBar = {
            AppBottomBar(navController = navController)
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

                    OrderRow(
                        order = order,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        color = AppTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

//the same as home
//@Preview(showSystemUi = true)
//@Composable
//fun MyOrderScreenPreview() {
//    TheCodeCupTheme {
//        MyOrderScreen(
//            ongoingOrders = previewOngoingOrders,
//            historyOrders = previewHistoryOrders
//        )
//    }
//}