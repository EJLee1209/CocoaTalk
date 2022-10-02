package com.dldmswo1209.cocoatalk.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

// message 테이블 구조
@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "room_id")
    var room_id: Int,
    @ColumnInfo(name = "sender_uid")
    var sender_uid: Int,
    @ColumnInfo(name = "receiver_uid")
    var receiver_uid: Int,
    @ColumnInfo(name = "text")
    var text: String,
    @ColumnInfo(name = "time")
    var time: String
)
