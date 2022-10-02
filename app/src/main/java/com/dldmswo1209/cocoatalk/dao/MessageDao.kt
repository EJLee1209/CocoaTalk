package com.dldmswo1209.cocoatalk.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dldmswo1209.cocoatalk.entity.MessageEntity

// SQLite 데이터베이스에 접근하기 위한 객체
@Dao
interface MessageDao {
    // roomId 로 해당 채팅방의 메시지 기록을 모두 가져옴
    @Query("SELECT * FROM message WHERE room_id=:roomId")
    fun getMessage(roomId: Int) : List<MessageEntity>

    // 메시지 저장
    @Insert
    fun saveMessage(message: MessageEntity)
}