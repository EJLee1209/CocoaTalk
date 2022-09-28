package com.dldmswo1209.cocoatalk.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository()

    private val _friendList = MutableLiveData<List<User>>()
    val friendList : LiveData<List<User>>
        get() = _friendList

    private val _isNewFriend = MutableLiveData<Boolean>()
    val isNewFriend : LiveData<Boolean>
        get() = _isNewFriend

    fun getAllMyFriend(user_id: String) = viewModelScope.launch {
        _friendList.postValue(repository.getAllMyFriend(user_id))
    }
    fun addFriend(user_id: String, friend_id: String) = viewModelScope.launch {
        _isNewFriend.postValue(repository.addFriend(user_id, friend_id))
    }
}