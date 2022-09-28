package com.dldmswo1209.cocoatalk.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = Repository()

    private val _currentUser = MutableLiveData<User>()
    val currentUser : LiveData<User>
        get() = _currentUser

    private val _isNew = MutableLiveData<Boolean>()
    val isNew : LiveData<Boolean>
        get() = _isNew

    fun login(id: String, password: String) = viewModelScope.launch {
        _currentUser.value = repository.login(id, password)
    }

    fun register(id: String, password: String, name: String) = viewModelScope.launch {
        _isNew.value = repository.register(id, password, name)
    }
}