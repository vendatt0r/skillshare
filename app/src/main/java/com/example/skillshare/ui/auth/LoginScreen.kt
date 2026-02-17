package com.example.skillshare.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.login,
            onValueChange = viewModel::onLoginChange,
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            Text(if (viewModel.isLoading) "Вход..." else "Войти")
        }
    }
}

class LoginViewModel : ViewModel() {
    private val _login = androidx.compose.runtime.mutableStateOf("")
    val login: String get() = _login.value

    private val _password = androidx.compose.runtime.mutableStateOf("")
    val password: String get() = _password.value

    private val _isLoading = androidx.compose.runtime.mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    fun onLoginChange(value: String) { _login.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    fun login(onSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
            onSuccess()
        }
    }
}

