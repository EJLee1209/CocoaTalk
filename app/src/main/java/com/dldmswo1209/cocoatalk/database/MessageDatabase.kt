package com.dldmswo1209.cocoatalk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dldmswo1209.cocoatalk.dao.MessageDao
import com.dldmswo1209.cocoatalk.entity.MessageEntity

@Database(entities = [MessageEntity::class], version=1)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object{
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(
            context: Context
        ) : MessageDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "messageDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}