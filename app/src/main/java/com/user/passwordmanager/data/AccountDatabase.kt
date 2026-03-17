package com.user.passwordmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Account::class, AccountLabel::class, Setting::class], version = 2, exportSchema = false)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun AccountDao(): AccountDAO
    abstract fun AccountLabelDao(): AccountLabelDAO
    abstract fun SettingDao(): SettingDAO
}
