package com.user.passwordmanager.data

import kotlinx.coroutines.flow.Flow

class AccountRepository (private val dao: AccountDAO) {
    val allAccount: Flow<List<Account>> = dao.getAllAccount()

    suspend fun insertAccount(insert: Account) = dao.insertAccount(insert)
    suspend fun deleteAccount(delete: Account) = dao.deleteAccount(delete)
    suspend fun searchWebsiteName(search: String) = dao.searchWebsiteName(search)
    suspend fun updateUsername(updateName: Account) = dao.updateUserName(updateName)
    suspend fun updatePassword(updatePassword: Account) = dao.updatePassword(updatePassword)
}