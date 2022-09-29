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

// 모든 레트로핏 통신 요청은 Repository 에서 관리
class Repository {
    private val retrofit = RetrofitInstance.getInstance().create(MyApi::class.java)

    // 로그인
    suspend fun login(id: String, password: String) = retrofit.login(id, password)

    // 회원가입
    suspend fun register(id: String, password: String, name: String) = retrofit.register(id, password,name)

    // 프로필 업데이트
    suspend fun profileUpdate(uid: Int, name: String, image: String?, state_msg: String?) = retrofit.profileUpdate(uid, name, image, state_msg)

    // 친구목록 조회
    suspend fun getAllMyFriend(user_id: String) = retrofit.getAllMyFriend(user_id)

    // 친구 추가
    suspend fun addFriend(user_id: String, friend_id: String) = retrofit.addFriend(user_id, friend_id)
}