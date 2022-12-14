package com.dedebkc.intermediate.ui.mainmenu.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dedebkc.intermediate.model.StoryModel
import com.dedebkc.intermediate.data.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var userStories: LiveData<PagingData<StoryModel>>

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getUserToken() = userRepository.getUserToken()

    fun getName(): LiveData<String> {
        return userRepository.getUserName()
    }

    fun getUserStories(token: String) {
        userStories = userRepository.getUserStoryList(token).cachedIn(viewModelScope)
    }
}