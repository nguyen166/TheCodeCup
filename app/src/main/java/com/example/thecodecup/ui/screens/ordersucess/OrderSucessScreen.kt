package com.example.thecodecup.ui.screens.ordersuccess

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun OrderSuccessScreen(
    onTrackOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.weight(1f))


            Icon(
                painter = painterResource(id = R.drawable.ic_order_success),
                contentDescription = null, // Icon này chỉ mang tính trang trí
                modifier = Modifier.size(150.dp),
                tint = AppTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = stringResource(id = R.string.order_success_title),
                style = AppTheme.typography.headlineLarge,
                color = AppTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thông điệp mô tả
            Text(
                text = stringResource(id = R.string.order_success_message),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.extendedColors.textGrey, // Màu xám #AAAAAA
                textAlign = TextAlign.Center
            )

            // Spacer để đẩy nút bấm xuống dưới
            Spacer(modifier = Modifier.weight(2f))

            // Nút "Track My Order"
            Button(
                onClick = onTrackOrderClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = AppTheme.shapes.large
            ) {
                Text(
                    text = stringResource(id = R.string.order_success_track_button),
                    style = AppTheme.typography.labelLarge
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun OrderSuccessScreenPreview() {
    TheCodeCupTheme {
        OrderSuccessScreen(
            onTrackOrderClick = {}
        )
    }
}