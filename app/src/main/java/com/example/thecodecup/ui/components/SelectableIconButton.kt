package com.example.thecodecup.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

@Composable
fun SelectableIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier ,
    iconModifier: Modifier = Modifier.size(16.dp)
) {
    val backgroundColor = if (isSelected) AppTheme.colorScheme.primary else AppTheme.colorScheme.background
    val iconColor = if (isSelected) AppTheme.colorScheme.outline else AppTheme.colorScheme.outline
    val borderColor = if (isSelected) AppTheme.colorScheme.primary else AppTheme.colorScheme.background

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(AppTheme.shapes.medium)
            .background(backgroundColor)
            .border(1.dp, borderColor, AppTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = iconModifier
        )
    }
}

@Preview
@Composable
fun SelectableIconButtonPreview() {
    TheCodeCupTheme {
        Row {
            SelectableIconButton(
                iconRes = R.drawable.ic_cold,
                contentDescription = "Selected",
                isSelected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.width(8.dp))
            SelectableIconButton(
                iconRes = R.drawable.ic_hot,
                contentDescription = "Not Selected",
                isSelected = false,
                onClick = {}
            )
        }
    }
}