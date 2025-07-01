package com.example.thecodecup.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Định nghĩa một file DataStore duy nhất
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(@ApplicationContext private val context: Context) {


    private object PreferencesKeys {
        val LOYALTY_STAMPS = intPreferencesKey("loyalty_stamps")
        val REWARD_POINTS = intPreferencesKey("reward_points")
    }

    // --- Loyalty Stamps (Thẻ tích điểm) ---

    /**
     * Một Flow phát ra số tem hiện tại mỗi khi nó thay đổi.
     * Mặc định là 0.
     */
    val loyaltyStampsFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LOYALTY_STAMPS] ?: 0
        }

    /**
     * Tăng số tem lên 1. Sẽ không vượt quá 8.
     */
    suspend fun incrementLoyaltyStamp() {
        context.dataStore.edit { settings ->
            val currentStamps = settings[PreferencesKeys.LOYALTY_STAMPS] ?: 0
            // Dùng coerceAtMost để đảm bảo giá trị không bao giờ lớn hơn 8
            settings[PreferencesKeys.LOYALTY_STAMPS] = (currentStamps + 1).coerceAtMost(8)
        }
    }

    /**
     * Reset số tem về 0 (sau khi đã đổi thưởng đủ 8 tem).
     */
    suspend fun resetLoyaltyStamps() {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.LOYALTY_STAMPS] = 0
        }
    }


    // --- Reward Points (Điểm thưởng) ---

    /**
     * Một Flow phát ra tổng số điểm thưởng hiện tại mỗi khi nó thay đổi.
     * Mặc định là 0.
     */
    val rewardPointsFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.REWARD_POINTS] ?: 0
        }

    /**
     * Cộng thêm một số điểm vào tổng điểm hiện tại.
     * Sẽ được gọi sau khi một đơn hàng được thanh toán thành công.
     */
    suspend fun addRewardPoints(pointsToAdd: Int) {
        context.dataStore.edit { settings ->
            val currentPoints = settings[PreferencesKeys.REWARD_POINTS] ?: 0
            settings[PreferencesKeys.REWARD_POINTS] = currentPoints + pointsToAdd
        }
    }

    /**
     * Trừ đi một số điểm sau khi người dùng đổi quà.
     */
    suspend fun redeemPoints(pointsToSpend: Int) {
        context.dataStore.edit { settings ->
            val currentPoints = settings[PreferencesKeys.REWARD_POINTS] ?: 0
            // Dùng coerceAtLeast để đảm bảo điểm không bao giờ bị âm
            settings[PreferencesKeys.REWARD_POINTS] = (currentPoints - pointsToSpend).coerceAtLeast(0)
        }
    }

    suspend fun getCurrentLoyaltyStamps(): Int {
        return loyaltyStampsFlow.first()
    }


}