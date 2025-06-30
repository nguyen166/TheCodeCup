package com.example.thecodecup.ui.screens.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.LoyaltyCard
import com.example.thecodecup.ui.components.MyPointsCard
import com.example.thecodecup.ui.components.RewardHistoryRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

// Dữ liệu giả
data class RewardHistory(val id: Int, val name: String, val date: String, val points: Int)
private val previewRewardHistory = listOf(
    RewardHistory(1, "Americano", "24 June | 12:30 PM", 12),
    RewardHistory(2, "Cafe Latte", "22 June | 08:30 AM", 12),
    RewardHistory(3, "Green Tea Latte", "16 June | 10:48 AM", 12),
    RewardHistory(4, "Flat White", "12 May | 11:25 AM", 12),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    stamps: Int,
    points: Int,
    history: List<RewardHistory>,
    onRedeemClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.rewards_screen_title)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.colorScheme.background)
            )
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
                    itemName = reward.name,
                    date = reward.date,
                    pointsEarned = reward.points
                )
                Divider(color = AppTheme.colorScheme.outline)
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun RewardsScreenPreview() {
    TheCodeCupTheme {
        RewardsScreen(
            stamps = 4,
            points = 2750,
            history = previewRewardHistory,
            onRedeemClick = {}
        )
    }
}