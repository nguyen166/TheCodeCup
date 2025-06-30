package com.example.thecodecup.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(1.dp, AppTheme.colorScheme.outline, CircleShape)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDecrease, enabled = quantity > 1) {
            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = "Decrease quantity",
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = quantity.toString(),
            style = AppTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = onIncrease) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "Increase quantity",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview
@Composable
fun QuantitySelectorPreview() {
    TheCodeCupTheme {
        QuantitySelector(quantity = 1, onIncrease = {}, onDecrease = {})
    }
}