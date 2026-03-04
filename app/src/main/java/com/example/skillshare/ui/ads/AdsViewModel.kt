package com.example.skillshare.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdsRepository) : ViewModel() {

    // 🔎 состояние поиска
    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _cityFilter = MutableStateFlow<String?>(null)

    // публичные геттеры (если понадобятся)
    val searchQuery: StateFlow<String?> = _searchQuery
    val cityFilter: StateFlow<String?> = _cityFilter

    // 🔥 главный поток объявлений с поиском
    val ads: StateFlow<List<AdEntity>> =
        combine(_searchQuery, _cityFilter) { query, city ->
            query to city
        }.flatMapLatest { (query, city) ->
            repository.searchAds(query, city)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun updateSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun updateCityFilter(city: String?) {
        _cityFilter.value = city
    }

    fun addAd(ad: AdEntity) {
        viewModelScope.launch {
            repository.addAd(ad)
        }
    }

    fun deleteAd(ad: AdEntity) {
        viewModelScope.launch {
            repository.deleteAd(ad)
        }
    }

    private val _selectedAd = MutableStateFlow<AdEntity?>(null)
    val selectedAd: StateFlow<AdEntity?> = _selectedAd

    fun loadAd(id: Long) {
        viewModelScope.launch {
            _selectedAd.value = repository.getAdById(id)
        }
    }

    fun updateAd(ad: AdEntity) {
        viewModelScope.launch {
            repository.updateAd(ad)
        }
    }

    fun getAdsByUser(userId: Long): StateFlow<List<AdEntity>> {
        return repository.getAdsByUser(userId)
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )
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
