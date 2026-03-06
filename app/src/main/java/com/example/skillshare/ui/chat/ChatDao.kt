package com.example.skillshare.ui.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert
    suspend fun insertMessage(message: ChatMessage)

    @Query("""
        SELECT * FROM chat_messages 
        WHERE exchangeId = :exchangeId 
        ORDER BY timestamp ASC
    """)
    fun getMessages(exchangeId: Long): Flow<List<ChatMessage>>
}