package com.dldmswo1209.cocoatalk.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository()

    private val _userInfo = MutableLiveData<User>()
    val userInfo : LiveData<User>
        get() = _userInfo

    private val _friendList = MutableLiveData<List<User>>()
    val friendList : LiveData<List<User>>
        get() = _friendList

    private val _isNewFriend = MutableLiveData<Boolean>()
    val isNewFriend : LiveData<Boolean>
        get() = _isNewFriend

    // 친구목록 조회
    fun getAllMyFriend(user_id: String) = viewModelScope.launch {
        // user_id 의 모든 친구를 불러옴
        _friendList.postValue(repository.getAllMyFriend(user_id))
    }
    // 친구추가
    fun addFriend(user_id: String, friend_id: String) = viewModelScope.launch {
        // user_id 가 friend_id 를 친구로 추가
        _isNewFriend.postValue(repository.addFriend(user_id, friend_id))
    }

    fun profileUpdate(uid: Int, name: String, image: String?, state_msg: String?) = viewModelScope.launch {
        repository.profileUpdate(uid, name, image, state_msg)
    }

    fun getUserInfo(id: String, password: String) = viewModelScope.launch {
        _userInfo.postValue(repository.login(id, password))
    }
}