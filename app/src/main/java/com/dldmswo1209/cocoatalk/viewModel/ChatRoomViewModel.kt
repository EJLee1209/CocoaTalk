package com.dldmswo1209.cocoatalk.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.launch

class ChatRoomViewModel: ViewModel() {
    private val repository = Repository()

    private val _isCreated = MutableLiveData<Boolean>()
    val isCreated : LiveData<Boolean>
        get() = _isCreated

    private val _findPerson = MutableLiveData<User>()
    val findPerson : LiveData<User>
        get() = _findPerson

    fun createChatRoom(from_id: String, to_id: String, subject: String, time: String) = viewModelScope.launch {
        _isCreated.value = repository.createChatRoom(from_id,to_id, subject, time)
    }
    fun findUser(id: String) = viewModelScope.launch {
        _findPerson.postValue(repository.findUser(id))
    }
}