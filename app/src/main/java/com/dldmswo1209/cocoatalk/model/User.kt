package com.dldmswo1209.cocoatalk.model

import java.io.Serializable

// 유저 정보를 저장하기 위한 DTO
data class User(
    var uid: Int, // primary key, auto increment
    var id: String,
    var password: String,
    var name: String,
    var image: String? = null, // 프로필 사진
    var state_msg: String? = null, // 상태 메시지
    var token: String? = null
) : Serializable
