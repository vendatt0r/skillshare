package com.example.skillshare.ui.ads

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class AdsViewModel : ViewModel() {

    private val _ads = mutableStateOf(
        listOf(
            Ad(1, "Урок английского", "Помогу подтянуть разговорный английский", "Москва", "Алексей"),
            Ad(2, "Ремонт компьютера", "Почищу ноутбук, переустановлю Windows", "Санкт-Петербург", "Иван"),
            Ad(3, "Дизайн логотипа", "Сделаю простой логотип", "Казань", "Мария")
        )
    )

    val ads: List<Ad>
        get() = _ads.value

    fun addAd(ad: Ad) {
        _ads.value = _ads.value + ad
    }
}
