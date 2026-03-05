package com.example.skillshare.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExchangeViewModel(private val repository: ExchangeRepository) : ViewModel() {

    private val _userExchanges = MutableStateFlow<List<Exchange>>(emptyList())
    val userExchanges: StateFlow<List<Exchange>> = _userExchanges

    fun loadUserExchanges(userId: Long) {
        viewModelScope.launch {
            repository.getUserExchanges(userId).collect {
                _userExchanges.value = it
            }
        }
    }

    fun proposeExchange(adId: Long, fromUserId: Long, toUserId: Long) = viewModelScope.launch {
        val exchange = Exchange(adId = adId, fromUserId = fromUserId, toUserId = toUserId)
        repository.proposeExchange(exchange)
    }

    fun acceptExchange(exchange: Exchange) = viewModelScope.launch {
        repository.updateExchange(exchange.copy(status = ExchangeStatus.ACCEPTED))
    }

    fun completeExchange(exchange: Exchange) = viewModelScope.launch {
        repository.updateExchange(exchange.copy(status = ExchangeStatus.COMPLETED))
    }

    fun cancelExchange(exchange: Exchange) = viewModelScope.launch {
        repository.updateExchange(exchange.copy(status = ExchangeStatus.CANCELLED))
    }
}