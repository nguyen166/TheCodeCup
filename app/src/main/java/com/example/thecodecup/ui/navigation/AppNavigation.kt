package com.example.thecodecup.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thecodecup.ui.screens.cart.CartRoute
import com.example.thecodecup.ui.screens.details.DetailsRoute
import com.example.thecodecup.ui.screens.details.DetailsViewModel
import com.example.thecodecup.ui.screens.home.HomeScreen
import com.example.thecodecup.ui.screens.home.HomeViewModel
import com.example.thecodecup.ui.screens.myorder.MyOrderRoute
import com.example.thecodecup.ui.screens.ordersuccess.OrderSuccessScreen
import com.example.thecodecup.ui.screens.profile.ProfileRoute
import com.example.thecodecup.ui.screens.profile.ProfileViewModel
import com.example.thecodecup.ui.screens.redeem.RedeemRoute
import com.example.thecodecup.ui.screens.rewards.RewardsRoute
import com.example.thecodecup.ui.screens.splash.SplashScreen
import com.example.thecodecup.ui.screens.vouchers.MyVouchersRoute
import androidx.compose.runtime.getValue
// Định nghĩa các route
object AppDestinations {
    const val SPLASH_ROUTE = "splash"
    const val HOME_ROUTE = "home"
    const val DETAILS_ROUTE = "details"
    const val CART_ROUTE = "cart"
    const val SUCCESS_ROUTE = "order success"
    const val PROFILE_ROUTE = "profile"
    const val REWARD_ROUTE = "reward"
    const val REDEEM_ROUTE = "redeem"
    const val MYORDER_ROUTE = "myorder"
    const val VOUCHERS_ROUTE = "vouchers"


    const val DETAILS_ID_ARG = "coffeeId"
    const val DETAILS_SHOT_ARG = "shot"
    const val DETAILS_SIZE_ARG = "size"
    const val DETAILS_ICE_ARG = "ice"
}


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    val profileViewModel: ProfileViewModel = hiltViewModel()



    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH_ROUTE // Bắt đầu từ Splash
    ) {
        // Màn hình Splash
        composable(route = AppDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(AppDestinations.HOME_ROUTE) {
                        popUpTo(AppDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Màn hình Home
        composable(route = AppDestinations.HOME_ROUTE) {
            val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()
            // Lấy ViewModel bằng Hilt
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = homeViewModel,
                onCoffeeClick = { coffeeId ->
                    // Logic điều hướng đến màn hình Details
                    navController.navigate("${AppDestinations.DETAILS_ROUTE}/$coffeeId")
                },
                onCartClick = {
                    navController.navigate(AppDestinations.CART_ROUTE)
                },
                onProfileClick = {
                    navController.navigate(AppDestinations.PROFILE_ROUTE)
                },
                navController = navController,
                onNavigateToReorder = { order ->
                    val address = profileUiState.userProfile?.address ?: ""
                    if (address.isBlank() || address.equals("unknown", ignoreCase = true)) {
                        navController.navigate(AppDestinations.PROFILE_ROUTE)
                    } else {
                        homeViewModel.reorderFromOrder(
                            order = order,
                            userAddress = address,
                            onCartSuccess = {
                                // Sau khi thêm thành công, điều hướng đến giỏ hàng
                                navController.navigate(AppDestinations.CART_ROUTE)
                            }
                        )
                    }
                }
            )
        }

        composable(
            route = "${AppDestinations.DETAILS_ROUTE}/{${AppDestinations.DETAILS_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.DETAILS_ID_ARG) { type = NavType.IntType })
        ) {
            DetailsRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToCart = { navController.navigate(AppDestinations.CART_ROUTE) },
                onNavigateToProfile = {navController.navigate(AppDestinations.PROFILE_ROUTE)}
            )
        }
        composable(route = AppDestinations.CART_ROUTE) {
            CartRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToOrderSuccess = {
                    // Điều hướng đến màn hình thành công và xóa CartScreen khỏi backstack
                    navController.navigate(AppDestinations.SUCCESS_ROUTE) {
                        popUpTo(AppDestinations.CART_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(route = AppDestinations.SUCCESS_ROUTE) {
            OrderSuccessScreen(
                onTrackOrderClick = {
                    navController.navigate(AppDestinations.MYORDER_ROUTE) {
                        popUpTo(AppDestinations.SUCCESS_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppDestinations.MYORDER_ROUTE) {
            MyOrderRoute(
                navController = navController
            )
        }

        composable(route = AppDestinations.PROFILE_ROUTE) {
            ProfileRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToVouchers = {
                    navController.navigate(AppDestinations.VOUCHERS_ROUTE)
                }
            )
        }
        composable(route = AppDestinations.REWARD_ROUTE) {
            RewardsRoute(
                onNavigateToRedeem = {
                    navController.navigate(AppDestinations.REDEEM_ROUTE)
                },
                navController = navController
            )
        }

        composable(route = AppDestinations.REDEEM_ROUTE) {
            RedeemRoute(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppDestinations.VOUCHERS_ROUTE) {
            MyVouchersRoute(
                onBackClick = { navController.popBackStack() }
            )
        }
    }


}