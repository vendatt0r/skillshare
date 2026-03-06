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
        val exchange = Exchange(
            adId = adId,
            fromUserId = fromUserId,
            toUserId = toUserId
        )
        repository.proposeExchange(exchange)
    }

    fun acceptExchange(exchange: Exchange, userId: Long) = viewModelScope.launch {
        repository.updateExchange(
            exchange.copy(status = ExchangeStatus.ACCEPTED)
        )
        loadUserExchanges(userId)
    }

    fun requestCompleteExchange(exchange: Exchange, userId: Long) = viewModelScope.launch {
        val updatedExchange = when (exchange.status) {
            ExchangeStatus.ACCEPTED -> exchange.copy(status = ExchangeStatus.COMPLETING)
            ExchangeStatus.COMPLETING -> exchange.copy(status = ExchangeStatus.COMPLETED)
            else -> return@launch
        }
        repository.updateExchange(updatedExchange)
        loadUserExchanges(userId)
    }

    fun cancelExchange(exchange: Exchange, userId: Long) = viewModelScope.launch {
        repository.updateExchange(
            exchange.copy(status = ExchangeStatus.CANCELLED)
        )
        loadUserExchanges(userId)
    }
}