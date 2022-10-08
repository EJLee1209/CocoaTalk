package com.dldmswo1209.cocoatalk.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

enum class ReadMessage{
    READ, NOTREAD
}

// message 테이블 구조
data class Message(
    var room_id: Int,
    var sender_uid: Int,
    var receiver_uid: Int,
    var text: String,
    var time: String,
    var flag: ReadMessage
)
