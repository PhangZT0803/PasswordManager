package com.user.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.data.AccountRepository
import com.user.passwordmanager.data.Setting
import com.user.passwordmanager.security.Security
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel (private val repository: AccountRepository): ViewModel(){
    val accounts: StateFlow<List<Account>> = repository.allAccount.map{
        accountList-> accountList.sortedBy {
            account -> account.accountId
        }
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun addAccount(webSiteName: String, userName: String, passwordText: String, webSiteUrl: String) = viewModelScope.launch {
        val encryptedPassword = Security.passwordEncryption(passwordText)
        val newAccount = Account(webSiteName = webSiteName, userName = userName, encryptedPassword = passwordText,webSiteUrl=webSiteUrl,labelId = null)
        repository.insertAccount(newAccount)
    }
    fun deleteAccount(account: Account) = viewModelScope.launch {
        repository.deleteAccount(account)
    }
    fun updateAccount(webSiteName: String, userName: String, passwordText: String, webSiteUrl: String) = viewModelScope.launch {
        val encryptedPassword = Security.passwordEncryption(passwordText)
        val newAccount = Account(webSiteName = webSiteName, userName = userName, encryptedPassword = passwordText,webSiteUrl=webSiteUrl,labelId = null)
        repository.updateAccount(newAccount)
    }

    fun saveAppPin(pin: String) = viewModelScope.launch {
        val setting = Setting(appPIN = pin, SetPIN = true)
        repository.saveSetting(setting)
    }
}