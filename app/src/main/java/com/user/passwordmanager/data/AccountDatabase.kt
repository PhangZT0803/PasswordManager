package com.user.passwordmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Account::class, Setting::class], version = 1, exportSchema = false)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun AccountDao(): AccountDAO
    abstract fun SettingDao(): SettingDAO
    //Database定义一个数据库有什么DAO和table.
}
