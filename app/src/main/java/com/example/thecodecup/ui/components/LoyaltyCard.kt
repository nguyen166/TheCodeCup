package com.example.thecodecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun LoyaltyCard(
    stamps: Int,
    totalStamps: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surfaceVariant
        )
    ) {
        // Column để chứa text và khu vực thẻ trắng
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Row chứa chữ "Loyalty card" và "4 / 8"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.home_loyalty_card_title),
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colorScheme.onSurfaceVariant // Màu trắng
                )
                Text(
                    text = stringResource(id = R.string.home_loyalty_status_format, stamps, totalStamps),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // KHU VỰC THẺ TRẮNG BÊN TRONG
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AppTheme.shapes.medium)
                    .background(AppTheme.colorScheme.background)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalStamps) { index ->
                    val isStamped = index < stamps
                    val iconRes = if (isStamped) {
                        R.drawable.ic_loyalty_cup_1
                    } else {
                        R.drawable.ic_loyalty_cup_0
                    }

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        // Quan trọng: Đối với SVG đa màu, chúng ta không dùng tint
                        // Đối với SVG một màu, chúng ta có thể dùng tint nếu cần
                        tint = if (isStamped) Color.Unspecified else AppTheme.extendedColors.textMuted.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF7F8FB)
@Composable
fun LoyaltyCardPreview() {
    TheCodeCupTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LoyaltyCard(stamps = 4, totalStamps = 8)
        }
    }
}