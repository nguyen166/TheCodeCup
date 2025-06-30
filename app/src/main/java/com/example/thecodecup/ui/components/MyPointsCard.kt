package com.example.thecodecup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
fun MyPointsCard(
    points: Int,
    onRedeemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surfaceVariant // Màu xanh đen
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.rewards_my_points_label),
                    style = AppTheme.typography.bodyLarge,
                    color = AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) // Màu trắng mờ
                )
                Text(
                    text = points.toString(),
                    style = AppTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.onSurfaceVariant // Màu trắng
                )
            }
            Button(
                onClick = onRedeemClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colorScheme.background,
                    contentColor = AppTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.rewards_redeem_button))
            }
        }
    }
}

@Preview
@Composable
fun MyPointsCardPreview() {
    TheCodeCupTheme {
        MyPointsCard(points = 2750, onRedeemClick = {})
    }
}