package com.example.skillshare.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    fun getMessages(exchangeId: Long): Flow<List<ChatMessage>> {
        return repository.getMessages(exchangeId)
    }

    fun sendMessage(exchangeId: Long, senderId: Long, text: String) {
        viewModelScope.launch {
            repository.sendMessage(
                ChatMessage(
                    exchangeId = exchangeId,
                    senderId = senderId,
                    message = text
                )
            )
        }
    }
}