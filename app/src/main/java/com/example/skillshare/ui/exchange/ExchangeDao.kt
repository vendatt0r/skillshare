package com.example.skillshare.ui.exchange

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {

    @Insert
    suspend fun insert(exchange: Exchange)

    @Update
    suspend fun update(exchange: Exchange)

    @Query("SELECT * FROM exchanges WHERE id = :id")
    suspend fun getById(id: Long): Exchange?

    @Query("SELECT * FROM exchanges WHERE fromUserId = :userId OR toUserId = :userId ORDER BY id DESC")
    fun getExchangesForUser(userId: Long): Flow<List<Exchange>>
}