package com.dldmswo1209.cocoatalk.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dldmswo1209.cocoatalk.entity.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE room_id=:roomId")
    fun getMessage(roomId: Int) : List<MessageEntity>

    @Insert
    fun saveMessage(message: MessageEntity)
}