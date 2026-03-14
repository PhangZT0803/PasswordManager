package com.user.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.user.passwordmanager.data.AccountRepository

class AccountViewModelFactory(private val repository: AccountRepository) : ViewModelProvider.Factory{
    override fun<T: ViewModel>create(modelClass:Class<T>):T{
        return AccountViewModel(repository) as T
    }
}