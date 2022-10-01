package com.dldmswo1209.cocoatalk.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.dldmswo1209.cocoatalk.entity.MessageEntity
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatRoomViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    private val repository = Repository(context)

    private val _isCreated = MutableLiveData<Boolean>()
    val isCreated : LiveData<Boolean>
        get() = _isCreated

    private val _findPerson = MutableLiveData<User>()
    val findPerson : LiveData<User>
        get() = _findPerson

    private val _messageList = MutableLiveData<List<MessageEntity>>()
    val messageList : LiveData<List<MessageEntity>>
        get() = _messageList


    fun createChatRoom(from_id: String, to_id: String, subject: String, time: String) = viewModelScope.launch {
        _isCreated.value = repository.createChatRoom(from_id,to_id, subject, time)
    }
    fun findUser(id: String) = viewModelScope.launch {
        _findPerson.postValue(repository.findUser(id))
    }

    fun getMessage(roomId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _messageList.postValue(repository.getMessage(roomId))
    }

    fun saveMessage(message: MessageEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveMessage(message)
    }
}