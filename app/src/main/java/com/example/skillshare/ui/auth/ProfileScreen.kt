package com.example.skillshare.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.skillshare.ui.ads.AdEntity
import com.example.skillshare.ui.ads.AdsViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    adsViewModel: AdsViewModel,
    onLogout: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser?.id) {
        adsViewModel.setProfileUser(currentUser?.id)
    }

    val userAds by adsViewModel.profileAds.collectAsState(initial = emptyList<AdEntity>())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
            .padding(16.dp)
    ) {

        Text("Профиль", style = MaterialTheme.typography.headlineMedium, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Имя: ${currentUser?.username}", color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Мои объявления", color = Color.White)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(userAds) { ad ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF262628)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {

                        Text(ad.title, color = Color.White)

                        Text(ad.description, color = Color.Gray)

                        Text(
                            "${ad.city} • ${ad.authorName}",
                            color = Color(0xFF0BB7F5)
                        )

                        TextButton(onClick = { onEdit(ad.id) }) {
                            Text("Редактировать", color = Color(0xFF0BB7F5))
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                adsViewModel.clearProfileAds()
                authViewModel.logout()
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти", color = Color.Black)
        }
    }
}