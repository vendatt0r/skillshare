package com.example.skillshare.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExchangeViewModelFactory(
    private val repository: ExchangeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}