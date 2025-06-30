package com.example.thecodecup.ui.screens.redeem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.RedeemItemRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme

// Dữ liệu giả cho việc xây dựng UI
// Đây cũng là một UI Model, không cần lưu vào CSDL
data class RedeemableItem(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val validUntil: String,
    val pointsCost: Int
)

private val previewRedeemableItems = listOf(
    RedeemableItem(1, "Cafe Latte", R.drawable.americano, "04.07.21", 1340),
    RedeemableItem(2, "Flat White", R.drawable.flatwhite, "04.07.21", 1340),
    RedeemableItem(3, "Cappuccino", R.drawable.capuchino, "04.07.21", 1340)
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    items: List<RedeemableItem>,
    onRedeemClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.redeem_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(id = R.string.cd_back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(items, key = { it.id }) { item ->
                RedeemItemRow(
                    imageRes = item.imageRes,
                    name = item.name,
                    validUntil = item.validUntil,
                    pointsCost = item.pointsCost,
                    onRedeemClick = { onRedeemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RedeemScreenPreview() {
    TheCodeCupTheme {
        RedeemScreen(
            items = previewRedeemableItems,
            onRedeemClick = {},
            onBackClick = {}
        )
    }
}