package com.dedebkc.intermediate.ui.mainmenu.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedebkc.intermediate.data.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserName(): LiveData<String> = userRepository.getUserName()

    fun getUserEmail(): LiveData<String> = userRepository.getUserEmail()

    fun getIsFirstTime(): LiveData<Boolean> = userRepository.getIsFirstTime()
    fun saveIsFirstTime(value: Boolean) {
        viewModelScope.launch {
            userRepository.saveIsFirstTime(value)
        }
    }

}