package com.example.thecodecup.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Định nghĩa DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val LOYALTY_STAMPS = intPreferencesKey("loyalty_stamps")
        val REWARD_POINTS = intPreferencesKey("reward_points")
    }

    val loyaltyStampsFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LOYALTY_STAMPS] ?: 0
        }

    suspend fun incrementLoyaltyStamp() {
        context.dataStore.edit { settings ->
            val currentStamps = settings[PreferencesKeys.LOYALTY_STAMPS] ?: 0
            settings[PreferencesKeys.LOYALTY_STAMPS] = (currentStamps + 1).coerceAtMost(8)
        }
    }

    // Thêm các hàm cho reward points...
}