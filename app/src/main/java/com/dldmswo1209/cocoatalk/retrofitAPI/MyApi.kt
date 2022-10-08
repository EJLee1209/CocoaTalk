package com.dldmswo1209.cocoatalk.retrofitAPI

import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.model.User
import retrofit2.http.*

// 레트로핏 통신 api
interface MyApi {

    // 사람 찾아요
    @GET("/user")
    suspend fun findUser(
        @Query("id") id: String
    ) : User

    // 로그인
    @GET("/user/login")
    suspend fun login(
        @Query("id") id: String,
        @Query("password") password: String
    ) : User

    // 회원가입
    @PUT("/user/register")
    suspend fun register(
        @Query("id") id: String,
        @Query("password") password: String,
        @Query("name") name: String
    ) : Boolean

    // 프로필 수정
    @POST("/user/update")
    suspend fun profileUpdate(
        @Query("uid") id: Int,
        @Query("name") name: String,
        @Query("image") image: String?,
        @Query("state_msg") state_msg: String?
    )

    // 친구목록 조회
    @GET("/friend/all")
    suspend fun getAllMyFriend(
        @Query("user_id") user_id: String
    ) : List<User>

    // 친구 추가
    @PUT("/friend/add")
    suspend fun addFriend(
        @Query("user_id") user_id: String,
        @Query("friend_id") friend_id: String
    ) : Boolean

    @GET("/my/room")
    suspend fun getAllMyChatRoom(
        @Query("user_id") user_id: String
    ) : List<ChatRoom>

    @PUT("/room/create")
    suspend fun createChatRoom(
        @Query("from_id") from_id: String,
        @Query("to_id") to_id: String,
        @Query("subject") subject: String,
        @Query("time") time: String
    ): Boolean

    @GET("/room/info")
    suspend fun getRoom(
        @Query("from_id") from_id: String,
        @Query("to_id") to_id: String
    ): ChatRoom

    @POST("/room/update")
    suspend fun updateRoom(
        @Query("room_id") room_id: Int,
        @Query("subject") subject: String,
        @Query("time") time: String
    )

    @POST("/register/token")
    suspend fun registerToken(
        @Query("uid") uid: Int,
        @Query("token") token: String
    )

    // 푸시 알림 전송
    @POST("/push")
    suspend fun sendPushMessage(
        @Query("token") token: String, // 받는 사람의 토큰
        @Query("from") from: String, // 보내는 사람 이름
        @Query("text") text: String // 메세지 내용
    )

    @GET("/get/message")
    suspend fun getMessage(
        @Query("room_id") room_id: Int
    ): List<Message>

    @PUT("/save/message")
    @Headers("accept: application/json",
        "content-type: application/json")
    suspend fun saveMessage(
        @Body jsonparams: Message
    )

}

data class MessageNumbers(
    var messageNumbers: Int
)