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

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: AccountRepository
) : ViewModel() {

    private val _setting = MutableStateFlow<Setting?>(null)
    val setting: StateFlow<Setting?> = _setting.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() = viewModelScope.launch {
        _setting.value = repository.getSetting() ?: Setting(appPIN = "", setPIN = false, mainTheme = 0, swipeDirection = 0)
    }

    fun updatePin(newPin: String) = viewModelScope.launch {
        _setting.value?.let { currentSetting ->
            val updatedSetting = currentSetting.copy(appPIN = newPin, setPIN = true)
            repository.saveSetting(updatedSetting)
            _setting.value = updatedSetting
        }
    }

    fun updateTheme(themeIndex: Int) = viewModelScope.launch {
        _setting.value?.let { currentSetting ->
            val updatedSetting = currentSetting.copy(mainTheme = themeIndex)
            repository.saveSetting(updatedSetting)
            _setting.value = updatedSetting
        }
    }

    fun updateSwipeDirection(swipeDirection:Int) = viewModelScope.launch {
        _setting.value?.let { current ->
            val updated = current.copy(swipeDirection = swipeDirection)
            repository.saveSetting(updated)
            _setting.value = updated
        }
    }

    fun reloadSettings() = viewModelScope.launch {
        _setting.value = repository.getSetting() ?: Setting(appPIN = "", setPIN = false, mainTheme = 0, swipeDirection = 0)
    }
}