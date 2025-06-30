package com.example.thecodecup.di

import android.content.Context
import androidx.room.Room
import com.example.thecodecup.data.local.AppDatabase
import com.example.thecodecup.data.local.dao.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "the_code_cup_db"
        ).build()
    }

    @Provides
    fun provideCartDao(appDatabase: AppDatabase): CartDao {
        return appDatabase.cartDao()
    }

    // Cung cấp các DAO khác tương tự...
}