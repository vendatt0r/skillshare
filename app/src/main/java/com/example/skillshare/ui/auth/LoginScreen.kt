package com.example.skillshare.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.skillshare.R

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF262628),
                unfocusedContainerColor = Color(0xFF262628),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color(0xFF00E676),
                unfocusedIndicatorColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF262628),
                unfocusedContainerColor = Color(0xFF262628),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color(0xFF00E676),
                unfocusedIndicatorColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.login(username, password) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Войти", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegisterClick) {
            Text("Регистрация", color = Color(0xFF0BB7F5))
        }

        authState?.let { success ->
            if (!success) {
                Text(
                    "Неверный логин или пароль",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LaunchedEffect(Unit) {
                    onLoginSuccess()
                    authViewModel.reset()
                }
            }
        }
    }
}