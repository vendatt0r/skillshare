package com.example.skillshare.ui.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditAdScreen(
    viewModel: AdsViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val ad by viewModel.selectedAd.collectAsState()

    ad?.let {

        var title by remember { mutableStateOf(it.title) }
        var description by remember { mutableStateOf(it.description) }
        var city by remember { mutableStateOf(it.city) }

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
                text = "Редактирование",
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
                    viewModel.updateAd(
                        it.copy(
                            title = title,
                            description = description,
                            city = city
                        )
                    )
                    onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E676)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Сохранить", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onBack) {
                Text("Назад", color = Color(0xFF0BB7F5))
            }
        }
    }
}