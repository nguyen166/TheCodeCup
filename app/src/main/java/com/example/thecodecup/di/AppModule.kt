package com.example.thecodecup.di

import android.content.Context
import androidx.room.Room
import com.example.thecodecup.data.local.AppDatabase
import com.example.thecodecup.data.local.dao.CartDao
import com.example.thecodecup.data.local.dao.OrderDao
import com.example.thecodecup.data.local.dao.UserProfileDao
import com.example.thecodecup.data.local.dao.VoucherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.inject.Provider


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

    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao {
        return appDatabase.orderDao()
    }

    @Provides
    fun provideUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return appDatabase.profileDao()
    }

    @Provides
    fun provideVoucherDao(appDatabase: AppDatabase): VoucherDao {
        return appDatabase.voucherDao()
    }


}