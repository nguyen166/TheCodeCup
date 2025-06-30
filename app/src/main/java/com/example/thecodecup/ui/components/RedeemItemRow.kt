package com.example.thecodecup.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun RedeemItemRow(
    @DrawableRes imageRes: Int,
    name: String,
    validUntil: String,
    pointsCost: Int,
    onRedeemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(R.string.cd_coffee_image, name),
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = AppTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.redeem_valid_until_format, validUntil),
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.extendedColors.textMuted
                )
            }
        }

        Button(
            onClick = onRedeemClick,
            shape = AppTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.primary,
                contentColor = AppTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.redeem_points_cost_format, pointsCost))
        }
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun RedeemItemRowPreview() {
    TheCodeCupTheme {
        RedeemItemRow(
            imageRes = R.drawable.americano,
            name = "Cafe Latte",
            validUntil = "04.07.21",
            pointsCost = 1340,
            onRedeemClick = {}
        )
    }
}