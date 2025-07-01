package com.example.thecodecup.ui.screens.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.LoyaltyCard
import com.example.thecodecup.ui.components.MyPointsCard
import com.example.thecodecup.ui.components.RewardHistoryRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.thecodecup.ui.components.AppBottomBar


private val previewRewardHistory = listOf(
    RewardHistoryItem(1, "Americano", "24 June | 12:30 PM", 12),
    RewardHistoryItem(2, "Cafe Latte", "22 June | 08:30 AM", 12),
    RewardHistoryItem(3, "Green Tea Latte", "16 June | 10:48 AM", 12),
    RewardHistoryItem(4, "Flat White", "12 May | 11:25 AM", 12),
)

@Composable
fun RewardsRoute(
    viewModel: RewardsViewModel = hiltViewModel(),
    onNavigateToRedeem: () -> Unit,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        RewardsScreen(
            stamps = uiState.stamps,
            points = uiState.points,
            history = uiState.history,
            onRedeemClick = onNavigateToRedeem,
            navController= navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    stamps: Int,
    points: Int,
    history: List<RewardHistoryItem>,
    onRedeemClick: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.rewards_screen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colorScheme.background)
            )
        },
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                LoyaltyCard(stamps = stamps, totalStamps = 8)
            }
            item {
                MyPointsCard(points = points, onRedeemClick = onRedeemClick)
            }
            item {
                Text(
                    text = stringResource(id = R.string.rewards_history_title),
                    style = AppTheme.typography.titleLarge
                )
            }
            items(history) { reward ->
                RewardHistoryRow(
                    itemName = reward.firstItemName,
                    date = reward.date,
                    pointsEarned = reward.pointsEarned
                )
                HorizontalDivider(
                    color = AppTheme.colorScheme.outline
                )
            }
        }
    }
}


