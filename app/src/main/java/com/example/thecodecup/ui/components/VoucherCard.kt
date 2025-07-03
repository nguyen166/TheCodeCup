package com.example.thecodecup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.data.local.model.Voucher
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun VoucherCard(
    voucher: Voucher,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick=onClick),
        shape = AppTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Có thể thêm icon voucher ở đây
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = voucher.title,
                    style = AppTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = voucher.description,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.extendedColors.textGrey
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

        }
    }
}

