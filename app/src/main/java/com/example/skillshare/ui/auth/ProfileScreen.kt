package com.example.skillshare.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skillshare.ui.ads.AdsViewModel
import com.example.skillshare.ui.ads.AdEntity

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    adsViewModel: AdsViewModel,
    onLogout: () -> Unit
) {
    // Получаем текущего пользователя
    val user by authViewModel.currentUser.collectAsState()

    // Flow объявлений текущего пользователя
    val userAdsFlow = adsViewModel.getAdsByUser(user?.id ?: -1L)
    val userAds by userAdsFlow.collectAsState(initial = emptyList<AdEntity>())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Профиль
        Text("Профиль", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Имя пользователя: ${user?.username ?: "Неизвестно"}")
        Spacer(modifier = Modifier.height(16.dp))

        // Список объявлений
        Text("Мои объявления", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f) // чтобы список занимал всё оставшееся место
        ) {
            items(userAds) { ad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(ad.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(ad.description)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${ad.city} • ${ad.authorName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка выхода
        Button(
            onClick = {
                authViewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Выйти")
        }
    }
}