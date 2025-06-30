package com.example.thecodecup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun RewardHistoryRow(
    itemName: String,
    date: String,
    pointsEarned: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = itemName,
                style = AppTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.extendedColors.textGrey
            )
        }
        Text(
            text = stringResource(id = R.string.rewards_points_earned_format, pointsEarned),
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RewardHistoryRowPreview() {
    TheCodeCupTheme {
        RewardHistoryRow(
            itemName = "Americano",
            date = "24 June | 12:30 PM",
            pointsEarned = 12
        )
    }
}