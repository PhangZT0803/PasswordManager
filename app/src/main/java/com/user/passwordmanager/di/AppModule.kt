package com.user.passwordmanager.di

import android.content.Context
import androidx.room.Room
import com.user.passwordmanager.data.AccountDatabase
import com.user.passwordmanager.data.AccountRepository
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
    fun provideDatabase(@ApplicationContext context: Context): AccountDatabase {
        return Room.databaseBuilder(
            context,
            AccountDatabase::class.java,
            "account_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(database: AccountDatabase): AccountRepository {
        return AccountRepository(database.AccountDao(), database.SettingDao())
    }
}