package com.dldmswo1209.cocoatalk.repository

import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.retrofitAPI.MyApi
import com.dldmswo1209.cocoatalk.retrofitAPI.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    private val retrofit = RetrofitInstance.getInstance().create(MyApi::class.java)

    suspend fun login(id: String, password: String) = retrofit.login(id, password)

    suspend fun register(id: String, password: String, name: String) = retrofit.register(id, password,name)

    suspend fun getAllMyFriend(user_id: String) = retrofit.getAllMyFriend(user_id)

    suspend fun addFriend(user_id: String, friend_id: String) = retrofit.addFriend(user_id, friend_id)
}