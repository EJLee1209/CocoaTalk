package com.dldmswo1209.cocoatalk.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.dldmswo1209.cocoatalk.entity.MessageEntity
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    private val repository = Repository(context)

    private val _userInfo = MutableLiveData<User>()
    val userInfo : LiveData<User>
        get() = _userInfo

    private val _friendList = MutableLiveData<List<User>>()
    val friendList : LiveData<List<User>>
        get() = _friendList

    private val _isNewFriend = MutableLiveData<Boolean>()
    val isNewFriend : LiveData<Boolean>
        get() = _isNewFriend

    private val _myChatRooms = MutableLiveData<List<ChatRoom>>()
    val myChatRooms : LiveData<List<ChatRoom>>
        get() = _myChatRooms

    private val _room = MutableLiveData<ChatRoom>()
    val room : LiveData<ChatRoom>
        get() = _room

    private val _isCreated = MutableLiveData<Boolean>()
    val isCreated : LiveData<Boolean>
        get() = _isCreated

    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList : LiveData<List<MessageEntity>>
        get() = _messageList

    private val _findPerson = MutableLiveData<User>()
    val findPerson : LiveData<User>
        get() = _findPerson

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

    fun getAllMyChatRoom(user_id: String) = viewModelScope.launch {
        _myChatRooms.postValue(repository.getAllMyChatRoom(user_id))
    }

    fun getRoom(from_id: String, to_id: String) = viewModelScope.launch {
        _room.postValue(repository.getRoom(from_id, to_id))
    }

    fun createChatRoom(from_id: String, to_id: String, subject: String, time: String) = viewModelScope.launch {
        _isCreated.value = repository.createChatRoom(from_id,to_id, subject, time)
    }

    fun getMessage(roomId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _messageList.postValue(repository.getMessage(roomId))
    }

    fun saveMessage(message: MessageEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveMessage(message)
    }

    fun findUser(id: String) = viewModelScope.launch {
        _findPerson.postValue(repository.findUser(id))
    }

    fun updateRoom(room_id: Int, subject: String,  time: String) = viewModelScope.launch {
        repository.updateRoom(room_id, subject, time)
    }

    fun registerToken(uid: Int, token: String) = viewModelScope.launch {
        repository.registerToken(uid, token)
    }

    fun sendPushMessage(token: String, from: String, text: String) = viewModelScope.launch {
        repository.sendPushMessage(token, from, text)
    }
}