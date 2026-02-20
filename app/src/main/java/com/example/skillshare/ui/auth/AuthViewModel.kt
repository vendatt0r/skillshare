package com.example.skillshare.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState: StateFlow<Boolean?> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.login(username, password)
            _currentUser.value = user
            _authState.value = user != null
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val exists = userDao.userExists(username) > 0
            if (!exists) {
                val user = User(username = username, password = password)
                userDao.insert(user)
                _currentUser.value = user
                _authState.value = true
            } else {
                _authState.value = false
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = null
    }

    fun reset() {
        _authState.value = null
    }
}