package com.example.thecodecup.ui.screens.redeem

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.thecodecup.R
import com.example.thecodecup.ui.components.RedeemItemRow
import com.example.thecodecup.ui.theme.AppTheme
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import androidx.compose.runtime.getValue


// Đây cũng là một UI Model, không cần lưu vào CSDL
data class RedeemableItem(
    val id: Int,
    val type: RedeemType, // <-- THÊM TRƯỜNG TYPE
    val name: String,
    val description: String? = null, // Thêm mô tả cho voucher
    val imageRes: Int,
    val pointsCost: Int
)

private val previewRedeemableItems = listOf(
    RedeemableItem(1, RedeemType.DRINK, "Cafe Latte", imageRes = R.drawable.americano, pointsCost = 1340),
    RedeemableItem(2,RedeemType.DRINK ,"Flat White", imageRes = R.drawable.flatwhite, pointsCost = 1340),
    RedeemableItem(3, RedeemType.DRINK,"Cappuccino", imageRes = R.drawable.capuchino, pointsCost = 1340),
    RedeemableItem(4, RedeemType.VOUCHER, "15% OFF Voucher", description = "For your next order", imageRes = R.drawable.ic_voucher, pointsCost = 1000),
    RedeemableItem(5, RedeemType.VOUCHER, "$1 OFF OFF Voucher", description = "For your next order", imageRes = R.drawable.ic_voucher, pointsCost = 1200)
)

@Composable
fun RedeemRoute(
    viewModel: RedeemViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    RedeemScreen(
        items = uiState.items,
        onRedeemClick = { itemId ->
            viewModel.redeemItem(itemId) { isSuccess,message ->
                if (isSuccess) {
                    Toast.makeText(context, "Redeemed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Not enough points!", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onBackClick = onBackClick
    )
}


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
                    description = item.description,
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