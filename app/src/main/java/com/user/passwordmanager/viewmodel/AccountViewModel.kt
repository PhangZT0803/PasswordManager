package com.user.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.data.AccountRepository
import com.user.passwordmanager.Domain.Security
import com.user.passwordmanager.Domain.PasswordStrength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//ViewModel是数据(respository)和UI的中间层,负责UI的互动逻辑
@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: AccountRepository): ViewModel() {
    val accounts: StateFlow<List<Account>> = repository.allAccount.map { accountList ->
        accountList.sortedBy { account ->
            account.accountId
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAccount(
        webSiteName: String,
        userName: String,
        passwordText: String,
        webSiteUrl: String
    ) = viewModelScope.launch {
        val encryptedPassword = Security.passwordEncryption(passwordText)
        val passwordStrength = PasswordStrength.evaluateStrength(passwordText)
        val newAccount = Account(
            webSiteName = webSiteName,
            userName = userName,
            encryptedPassword = encryptedPassword,
            webSiteUrl = webSiteUrl,
            passwordStrength = passwordStrength,
        )
        repository.insertAccount(newAccount)
    }

    fun deleteAccount(account: Account) = viewModelScope.launch {
        repository.deleteAccount(account)
    }

    fun updateAccount(
        accountId: Int,
        webSiteName: String,
        userName: String,
        passwordText: String,
        webSiteUrl: String
    ) = viewModelScope.launch {
        val encryptedPassword = Security.passwordEncryption(passwordText)
        val passwordStrength = PasswordStrength.evaluateStrength(passwordText)
        val updateAccount = Account(
            accountId = accountId,
            webSiteName = webSiteName,
            userName = userName,
            encryptedPassword = encryptedPassword,
            webSiteUrl = webSiteUrl,
            passwordStrength = passwordStrength,
        )
        repository.updateAccount(updateAccount)
    }
}