package com.example.thecodecup.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.thecodecup.ui.theme.AppTheme

@Composable
fun SelectableButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) AppTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) AppTheme.colorScheme.onPrimary else AppTheme.colorScheme.onBackground

    Button(
        onClick = onClick,
        shape = AppTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = if (isSelected) ButtonDefaults.buttonElevation() else null,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(text)
    }
}