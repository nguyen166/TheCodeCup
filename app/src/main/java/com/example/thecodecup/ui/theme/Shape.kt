package com.example.thecodecup.ui.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(16.dp), // Độ bo góc cho các Card nhỏ
    large = RoundedCornerShape(24.dp)  // Độ bo góc cho các Card lớn, nút bấm
)