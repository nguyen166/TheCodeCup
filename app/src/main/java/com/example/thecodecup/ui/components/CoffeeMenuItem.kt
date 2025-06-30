package com.example.thecodecup.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun CoffeeMenuItem(
    @DrawableRes imageRes: Int,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Card chỉ đóng vai trò là container với bo góc và xử lý click
    Card(
        modifier = modifier
            .aspectRatio(0.8f) // Tỷ lệ chiều rộng/chiều cao để thẻ trông dài hơn một chút
            .clickable(onClick = onClick),
        shape = AppTheme.shapes.large, // RoundedCornerShape(24.dp)
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Thêm chút bóng đổ
    ) {
        // Box cho phép xếp chồng các thành phần
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Lớp dưới cùng: Hình ảnh
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(R.string.cd_coffee_image, name),
                modifier = Modifier.fillMaxSize(),
                // Dùng ContentScale.Crop để ảnh lấp đầy Box mà không bị méo
                contentScale = ContentScale.Crop
            )

            // 2. Lớp ở giữa: Lớp phủ gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 100f // Bắt đầu gradient từ khoảng 1/3 ảnh
                        )
                    )
            )

            // 3. Lớp trên cùng: Tên món ăn
            Text(
                text = name,
                style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart) // Căn chỉnh ở góc dưới bên trái
                    .padding(16.dp)
            )
        }
    }
}


@Preview(widthDp = 180)
@Composable
fun CoffeeMenuItemPreview() {
    TheCodeCupTheme {
        CoffeeMenuItem(
            imageRes = R.drawable.americano,
            name = "Americano",
            onClick = {}
        )
    }
}