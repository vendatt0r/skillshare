package com.example.skillshare.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdsRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _cityFilter = MutableStateFlow<String?>(null)
    private val _currentUserId = MutableStateFlow<Long?>(null)
    private val _profileUserId = MutableStateFlow<Long?>(null)

    // Главная лента (исключаем текущего пользователя)
    val mainAds: StateFlow<List<AdEntity>> = combine(
        _searchQuery, _cityFilter, _currentUserId
    ) { query, city, currentUserId ->
        Triple(query, city, currentUserId)
    }.flatMapLatest { (query, city, currentUserId) ->
        repository.getAdsExcludingUser(currentUserId, query, city)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Лента для профиля конкретного пользователя
    val profileAds: StateFlow<List<AdEntity>> = _profileUserId.flatMapLatest { userId ->
        if (userId == null) flowOf(emptyList())
        else repository.getAdsByUser(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedAd = MutableStateFlow<AdEntity?>(null)
    val selectedAd: StateFlow<AdEntity?> = _selectedAd

    fun setCurrentUser(userId: Long?) {
        _currentUserId.value = userId
    }

    fun setProfileUser(userId: Long?) {
        _profileUserId.value = userId
    }

    fun updateSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun updateCityFilter(city: String?) {
        _cityFilter.value = city
    }

    fun addAd(ad: AdEntity) = viewModelScope.launch { repository.addAd(ad) }
    fun deleteAd(ad: AdEntity) = viewModelScope.launch { repository.deleteAd(ad) }
    fun loadAd(id: Long) = viewModelScope.launch { _selectedAd.value = repository.getAdById(id) }
    fun updateAd(ad: AdEntity) = viewModelScope.launch { repository.updateAd(ad) }
    fun clearProfileAds() {
        _profileUserId.value = null
    }
}

class AdsViewModelFactory(
    private val repository: AdsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
