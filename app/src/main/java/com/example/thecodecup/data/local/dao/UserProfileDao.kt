package com.example.thecodecup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thecodecup.data.local.model.UserProfile
import kotlinx.coroutines.flow.Flow


@Dao
interface UserProfileDao  {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(userProfile: UserProfile)


}