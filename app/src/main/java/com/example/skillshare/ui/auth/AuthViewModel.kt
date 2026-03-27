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

    // 🔹 LOGIN
    fun login(username: String, password: String) {
        viewModelScope.launch {

            if (username.isBlank() || password.isBlank()) {
                _authState.value = false
                return@launch
            }

            val user = userDao.getUserByUsername(username)

            val hashed = PasswordUtils.hash(password)

            if (user != null && user.passwordHash == hashed) {
                _currentUser.value = user
                _authState.value = true
            } else {
                _authState.value = false
            }
        }
    }

    // 🔹 REGISTER
    fun register(username: String, password: String) {
        viewModelScope.launch {

            if (username.length < 3 || password.length < 4) {
                _authState.value = false
                return@launch
            }

            val exists = userDao.userExists(username) > 0

            if (exists) {
                _authState.value = false
                return@launch
            }

            val user = User(
                username = username,
                passwordHash = PasswordUtils.hash(password)
            )

            val id = userDao.insert(user)

            _currentUser.value = user.copy(id = id)
            _authState.value = true
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