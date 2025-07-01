package com.example.thecodecup.ui.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.CoffeeMenuItem
import com.example.thecodecup.ui.components.LoyaltyCard
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.thecodecup.data.local.model.Coffee
import com.example.thecodecup.data.local.model.staticCoffeeList
import com.example.thecodecup.ui.components.AppBottomBar


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCoffeeClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavController
) {
    // Lấy state từ ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        userName = uiState.userName,
        stamps = uiState.stamps,
        coffeeList = uiState.coffeeList,
        onCoffeeClick = onCoffeeClick,
        onCartClick = onCartClick,
        onProfileClick = onProfileClick,
        navController = navController
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    userName: String,
    stamps: Int,
    coffeeList: List<Coffee>,
    onCoffeeClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    navController: NavController
) {
    Scaffold(

        containerColor = AppTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = stringResource(id = R.string.home_greeting),
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.extendedColors.textMuted
                        )
                        Text(
                            text = userName,
                            style = AppTheme.typography.titleLarge,
                            color = AppTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart_checkout),
                            contentDescription = stringResource(id = R.string.cd_cart_button),
                            tint = AppTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = stringResource(id = R.string.cd_profile_button),
                            tint = AppTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Màu nền trong suốt
                )
            )
        },
        bottomBar = {
            AppBottomBar(navController=navController)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(AppTheme.colorScheme.surfaceVariant)
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                // 1. LoyaltyCard nằm BÊN TRONG, ngay đầu tiên
                LoyaltyCard(stamps = stamps, totalStamps = 8)

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Tiêu đề "Choose your coffee"
                Text(
                    text = stringResource(id = R.string.home_choose_coffee_title),
                    style = AppTheme.typography.titleLarge,
                    color = AppTheme.colorScheme.onSurfaceVariant // Màu trắng
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Lưới sản phẩm
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),

                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 16.dp)
                ) {
                    items(coffeeList, key = { it.id }) { coffee ->
                        CoffeeMenuItem(
                            imageRes = coffee.imageRes,
                            name = coffee.name,
                            onClick = { onCoffeeClick(coffee.id) }
                        )
                    }
                }
            }
        }
    }
}

// Dữ liệu giả để xây dựng UI





//it have navcontroller so we not need preview for it later. ( if want to preview just remove navcontroller)

//@Preview(showSystemUi = true, device = "id:pixel_5")
//@Composable
//fun HomeScreenPreview() {
//    TheCodeCupTheme {
//        HomeScreenContent(
//            userName = "Anderson",
//            stamps = 4,
//            coffeeList = staticCoffeeList,
//            onCoffeeClick = {},
//            onCartClick = {},
//            onProfileClick = {}
//
//        )
//    }
//}