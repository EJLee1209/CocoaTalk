package com.dldmswo1209.cocoatalk.repository

import android.content.Context
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.retrofitAPI.MyApi
import com.dldmswo1209.cocoatalk.retrofitAPI.RetrofitInstance

// 모든 레트로핏 통신 요청은 Repository 에서 관리
class Repository(context: Context) {
    private val retrofit = RetrofitInstance.getInstance().create(MyApi::class.java)

    suspend fun findUser(id: String) = retrofit.findUser(id)

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

    suspend fun getAllMyChatRoom(user_id: String) = retrofit.getAllMyChatRoom(user_id)

    suspend fun createChatRoom(from_id: String, to_id: String, subject: String, time: String) = retrofit.createChatRoom(from_id, to_id, subject, time)

    suspend fun saveMessage(message: Message) = retrofit.saveMessage(message)

    suspend fun getMessage(roomId: Int) = retrofit.getMessage(roomId)

    suspend fun getRoom(from_id: String, to_id: String) = retrofit.getRoom(from_id, to_id)

    suspend fun updateRoom(room_id: Int, subject: String, time: String) = retrofit.updateRoom(room_id, subject, time)

    suspend fun registerToken(uid: Int, token:String) = retrofit.registerToken(uid, token)

    suspend fun sendPushMessage(token: String, from: String, text: String) = retrofit.sendPushMessage(token, from, text)
}