package com.example.thecodecup.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R

@Composable
fun NavigationRow(
    label: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Làm cho cả hàng có thể nhấn được
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon đầu dòng (giống ProfileInfoRow)
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f) // Chiếm hết không gian còn lại
        )

        // Icon mũi tên chỉ sang phải ở cuối
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward), // Cần tạo icon này
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}