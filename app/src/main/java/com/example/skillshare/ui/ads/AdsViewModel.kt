package com.example.skillshare.ui.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdsRepository) : ViewModel() {

    val ads: StateFlow<List<AdEntity>> =
        repository.getAllAds()
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )

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
