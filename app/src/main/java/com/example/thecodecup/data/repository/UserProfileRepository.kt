package com.example.thecodecup.data.repository

import com.example.thecodecup.data.local.dao.UserProfileDao // Cần tạo DAO này
import com.example.thecodecup.data.local.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val profileDao: UserProfileDao) {
    fun getUserProfile(): Flow<UserProfile?> = profileDao.getUserProfile()

    suspend fun updateUserProfile(userProfile: UserProfile) {
        profileDao.insertOrUpdateProfile(userProfile)
    }
}