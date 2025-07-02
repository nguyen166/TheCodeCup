package com.example.thecodecup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.ui.theme.AppTheme

@Composable
fun RecentOrderSuggestion(
    latestOrder: Order,
    onReorderClick: (Order) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lấy tên các món hàng từ đơn hàng
    val itemNames = latestOrder.items.joinToString(", ") { it.name }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Re-order your usual?",
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = itemNames,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.extendedColors.textMuted,
                    maxLines = 1 // Chỉ hiển thị 1 dòng
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { onReorderClick(latestOrder) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_reorder),
                    contentDescription = "Re-order"
                )
            }
        }
    }
}

