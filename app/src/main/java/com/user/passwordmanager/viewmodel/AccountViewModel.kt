package com.user.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.data.AccountRepository
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
    fun addAccount(webSiteName: String, userName: String, passwordText: String) = viewModelScope.launch {
        val newAccount = Account(webSiteName = webSiteName, userName = userName, encryptedPassword = passwordText,webSiteUrl=" ",labelId = null)
        repository.insertAccount(newAccount)
    }
    fun deleteAccount(account: Account) = viewModelScope.launch {
        repository.deleteAccount(account)
    }
    fun updateUsername(account: Account) = viewModelScope.launch {
        repository.updateUsername(account)
    }
    fun updatePassword(account:Account)= viewModelScope.launch {
        repository.updatePassword(account)
    }
    fun searchWebsiteName(search: String) = viewModelScope.launch {
        repository.searchWebsiteName(search)
    }

}