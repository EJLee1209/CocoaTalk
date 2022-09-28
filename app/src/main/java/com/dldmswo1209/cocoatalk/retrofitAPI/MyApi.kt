package com.dldmswo1209.cocoatalk.retrofitAPI

import com.dldmswo1209.cocoatalk.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

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

    @GET("/friend/all")
    suspend fun getAllMyFriend(
        @Query("user_id") user_id: String
    ) : List<User>

    @PUT("friend/add")
    suspend fun addFriend(
        @Query("user_id") user_id: String,
        @Query("friend_id") friend_id: String
    ) : Boolean

}