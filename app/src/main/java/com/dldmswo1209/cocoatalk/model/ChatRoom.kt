package com.dldmswo1209.cocoatalk.model

data class ChatRoom(
    val id: Int,
    val from_id: String,
    val to_id: String,
    val subject: String,
    val time: String,
    val from_name: String,
    val to_name: String,
    val from_image: String?,
    val to_image: String?
)
