package com.example.thecodecup.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Luôn chỉ có 1 profile
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)

