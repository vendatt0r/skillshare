package com.example.skillshare.ui.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CreateAdScreen(
    currentUserId: Long,
    currentUsername: String,
    onAdCreated: (AdEntity) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF262628),
        unfocusedContainerColor = Color(0xFF262628),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedIndicatorColor = Color(0xFF00E676),
        unfocusedIndicatorColor = Color.Gray
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
            .padding(24.dp)
    ) {

        Text(
            text = "Создание объявления",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Город") },
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val newAd = AdEntity(
                    title = title,
                    description = description,
                    city = city,
                    authorName = currentUsername,
                    userId = currentUserId
                )
                onAdCreated(newAd)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = title.isNotBlank() && description.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00E676)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Создать", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("Назад", color = Color(0xFF0BB7F5))
        }
    }
}