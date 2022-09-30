package com.dldmswo1209.cocoatalk.retrofitAPI

import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// 레트로핏 통신 api
interface MyApi {
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

}