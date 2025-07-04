package com.example.thecodecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.data.local.model.Order
import com.example.thecodecup.ui.screens.myorder.formatOrderTimestamp
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import java.text.NumberFormat
import java.util.*

@Composable
fun OrderRow(
    order: Order,
    modifier: Modifier = Modifier
) {
    val itemNames = order.items.map { "${it.name} (x${it.quantity})" }
    val dateString = formatOrderTimestamp(order.timestamp)
    val price = order.totalPrice
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp).background( AppTheme.colorScheme.surface )
    ) {
        // Hàng trên: Ngày tháng và Giá tiền
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dateString,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.extendedColors.textMuted
            )
            Text(
                text = formatCurrency(price),
                style = AppTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = AppTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(16.dp))

        // Phần thông tin chi tiết
        // Hiển thị tên các món hàng
        itemNames.forEach { name ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_size),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppTheme.extendedColors.textGrey
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Dòng địa chỉ
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_location_1),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppTheme.extendedColors.textGrey
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = order.address,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.onBackground
            )
        }
    }
}


private fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    format.maximumFractionDigits = 2
    return format.format(price)
}
