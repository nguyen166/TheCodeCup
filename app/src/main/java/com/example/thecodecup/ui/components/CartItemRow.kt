package com.example.thecodecup.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import java.text.NumberFormat
import java.util.*
import com.example.thecodecup.data.local.model.CartItem

@Composable
fun CartItemRow(
    item: CartItem,
    modifier: Modifier = Modifier
) {
    val options = "${item.shot} | ${item.ice} | ${item.size}"
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surface // Màu nền #F7F8FB
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = stringResource(R.string.cd_coffee_image, item.name),
                modifier = Modifier
                    .size(80.dp)
                    .clip(AppTheme.shapes.medium), // Bo góc cho ảnh
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f), // Chiếm hết không gian còn lại
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.onSurface
                )
                Text(
                    text = options,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.extendedColors.textGrey
                )
                Text(
                    text = stringResource(id = R.string.cart_item_quantity_format, item.quantity),
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colorScheme.onSecondary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = formatCurrency(item.price),
                style = AppTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colorScheme.onSurface
            )
        }
    }
}

// Hàm tiện ích để format tiền tệ
private fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    format.maximumFractionDigits = 2
    return format.format(price)
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CartItemRowPreview() {
    TheCodeCupTheme {
        val previewItem = CartItem(1, 1, "Cappuccino", "Cold",3.0, 1, "medium", "single", "full ice", R.drawable.capuchino,"unknown")
        CartItemRow(item = previewItem)
    }
}