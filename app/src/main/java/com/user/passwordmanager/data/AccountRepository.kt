package com.user.passwordmanager.data

import kotlinx.coroutines.flow.Flow

//Respository是唯一一个知道数据来源的地方,负责管数据去哪里和数据从哪里来.
class AccountRepository (private val dao: AccountDAO, private val settingDao: SettingDAO) {
    val allAccount: Flow<List<Account>> = dao.getAllAccount()
    suspend fun insertAccount(insert: Account) = dao.insertAccount(insert)
    suspend fun deleteAccount(delete: Account) = dao.deleteAccount(delete)
    suspend fun updateAccount(update: Account) = dao.updateAccount(update)

    suspend fun getSetting(): Setting? = settingDao.getSetting()
    suspend fun saveSetting(setting: Setting) = settingDao.insertSetting(setting)
}