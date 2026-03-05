package com.example.skillshare.ui.exchange

import kotlinx.coroutines.flow.Flow

class ExchangeRepository(private val dao: ExchangeDao) {

    suspend fun proposeExchange(exchange: Exchange) {
        dao.insert(exchange)
    }

    suspend fun updateExchange(exchange: Exchange) {
        dao.update(exchange)
    }

    suspend fun getExchangeById(id: Long): Exchange? {
        return dao.getById(id)
    }

    fun getUserExchanges(userId: Long): Flow<List<Exchange>> {
        return dao.getExchangesForUser(userId)
    }
}