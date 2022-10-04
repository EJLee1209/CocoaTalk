package com.dldmswo1209.cocoatalk.retrofitAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 레트로핏 객체 생성을 위한 object
object RetrofitInstance {
    val BASE_URL = "https://e2fc-119-67-181-215.jp.ngrok.io"

    val client = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance(): Retrofit{
        return client
    }
}