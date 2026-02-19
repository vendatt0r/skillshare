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
}
