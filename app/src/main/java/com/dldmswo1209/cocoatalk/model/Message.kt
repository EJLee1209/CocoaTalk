package com.dldmswo1209.cocoatalk.model

data class Message(
    val id: Int,
    var sender_uid: Int,
    var message: String,
    var time: String
)