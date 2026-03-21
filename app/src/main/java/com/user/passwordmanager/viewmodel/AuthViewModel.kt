package com.user.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.user.passwordmanager.data.AccountRepository
import com.user.passwordmanager.data.Setting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class AuthState {
    object Loading : AuthState()
    object Registering : AuthState()
    data class RequireLogin(val correctPin: String) : AuthState()
    object Authenticated : AuthState()
}
@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AccountRepository): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkInitialState()
    }

    private fun checkInitialState() = viewModelScope.launch {
        val setting = repository.getSetting()
        if (setting != null && setting.SetPIN) {
            _authState.value = AuthState.RequireLogin(setting.appPIN)
        } else {
            _authState.value = AuthState.Registering
        }
    }

    fun saveAppPin(pin: String) = viewModelScope.launch {
        val setting = Setting(appPIN = pin, SetPIN = true)
        repository.saveSetting(setting)
        _authState.value = AuthState.Authenticated
    }

    fun setAuthenticated() {
        _authState.value = AuthState.Authenticated
    }
}