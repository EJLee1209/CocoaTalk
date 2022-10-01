package com.dldmswo1209.cocoatalk.model

data class Message(
    var sender_uid: Int,
    var receiver_uid: Int,
    var message: String,
    var time: String
)