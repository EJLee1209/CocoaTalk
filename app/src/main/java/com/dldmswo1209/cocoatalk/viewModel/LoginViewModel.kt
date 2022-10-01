package com.dldmswo1209.cocoatalk.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.repository.Repository
import kotlinx.coroutines.launch

// 로그인 액티비티에서 사용하기 위한 뷰모델
// 로그인/회원가입을 위한 정보를 저장
class LoginViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>().applicationContext
    private val repository = Repository(context)

    private val _currentUser = MutableLiveData<User>()
    val currentUser : LiveData<User>
        get() = _currentUser

    private val _isNew = MutableLiveData<Boolean>()
    val isNew : LiveData<Boolean>
        get() = _isNew

    // 로그인
    fun login(id: String, password: String) = viewModelScope.launch {
        // repository 에서 로그인을 서버에 요청하면 database 에서 id 와 password 를 비교하고
        // 일치하면, 해당 User 를 리턴, 일치하지 않으면 User(-1,"","","","","") 를 리턴
        _currentUser.value = repository.login(id, password)
    }

    // 회원가입
    fun register(id: String, password: String, name: String) = viewModelScope.launch {
        //repository 에서 회원가입을 서버에 요청하면 이미 id가 DB에 존재하는 경우 false 아니면 DB에 추가하고 true 리턴
        _isNew.value = repository.register(id, password, name)
    }
}