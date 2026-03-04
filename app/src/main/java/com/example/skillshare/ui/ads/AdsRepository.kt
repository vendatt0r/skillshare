package com.example.skillshare.ui.ads

import kotlinx.coroutines.flow.Flow

// Можно сделать с @Singleton, если используешь Hilt, но сейчас просто простой класс
class AdsRepository(private val adDao: AdDao) {

    // Получаем все объявления
    fun getAllAds(): Flow<List<AdEntity>> = adDao.getAllAds()

    // Добавляем новое объявление
    suspend fun addAd(ad: AdEntity) {
        adDao.insert(ad)
    }

    suspend fun deleteAd(ad: AdEntity) {
        adDao.deleteAd(ad)
    }
    suspend fun getAdById(id: Long): AdEntity? {
        return adDao.getAdById(id)
    }

    suspend fun updateAd(ad: AdEntity) {
        adDao.updateAd(ad)
    }

    fun getAdsByUser(userId: Long): Flow<List<AdEntity>> {
        return adDao.getAdsByUser(userId)
    }

    fun searchAds(query: String?, city: String?): Flow<List<AdEntity>> {
        return adDao.searchAds(query, city)
    }
}
