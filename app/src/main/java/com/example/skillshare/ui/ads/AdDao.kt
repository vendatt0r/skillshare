package com.example.skillshare.ui.ads

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AdDao {

    @Insert
    suspend fun insert(ad: AdEntity)

    @Query("SELECT * FROM ads ORDER BY id DESC")
    fun getAllAds(): Flow<List<AdEntity>>

    @Delete
    suspend fun deleteAd(ad: AdEntity)
    @Query("SELECT * FROM ads WHERE id = :id")
    suspend fun getAdById(id: Long): AdEntity?

    @Update
    suspend fun updateAd(ad: AdEntity)
}
