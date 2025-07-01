package com.example.thecodecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thecodecup.R
import com.example.thecodecup.ui.navigation.AppDestinations
import com.example.thecodecup.ui.theme.AppTheme

sealed class BottomNavItem(val route: String, val iconResId: Int, val contentDescriptionResId: Int) {
    object Home : BottomNavItem(AppDestinations.HOME_ROUTE, R.drawable.ic_nav_home, R.string.bottom_nav_home)
    object Rewards : BottomNavItem(AppDestinations.REWARD_ROUTE, R.drawable.ic_nav_reward, R.string.bottom_nav_rewards)
    object MyOrders : BottomNavItem(AppDestinations.MYORDER_ROUTE, R.drawable.ic_nav_orders, R.string.bottom_nav_orders)
}

@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Rewards,
        BottomNavItem.MyOrders
    )

    // Dùng Box để tạo padding xung quanh thanh bar
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(AppTheme.colorScheme.surface)
                .padding(horizontal = 16.dp), // Thêm padding bên trong Row
            horizontalArrangement = Arrangement.SpaceAround, // Chia đều không gian
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            navItems.forEach { item ->
                IconButton(
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    // Modifier cho IconButton để tăng vùng có thể nhấn
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = stringResource(id = item.contentDescriptionResId),
                        // Thay đổi màu dựa trên việc item có được chọn hay không
                        tint = if (currentRoute == item.route) {
                            AppTheme.colorScheme.primary
                        } else {
                            AppTheme.extendedColors.textGrey
                        }
                    )
                }
            }
        }
    }
}