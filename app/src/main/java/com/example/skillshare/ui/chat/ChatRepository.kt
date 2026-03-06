package com.example.skillshare.ui.chat

import kotlinx.coroutines.flow.Flow

class ChatRepository(private val dao: ChatDao) {

    suspend fun sendMessage(message: ChatMessage) {
        dao.insertMessage(message)
    }

    fun getMessages(exchangeId: Long): Flow<List<ChatMessage>> {
        return dao.getMessages(exchangeId)
    }
}