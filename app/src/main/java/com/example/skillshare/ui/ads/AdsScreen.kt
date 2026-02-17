package com.example.skillshare.ui.ads

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdsScreen(
    viewModel: AdsViewModel = viewModel()
) {
    val ads = viewModel.ads

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = {
                viewModel.addAd(
                    Ad(
                        id = System.currentTimeMillis(),
                        title = "Новое объявление",
                        description = "Описание нового объявления",
                        city = "Москва",
                        authorName = "Вы"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить объявление")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(ads) { ad ->
                AdItem(ad)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AdItem(ad: Ad) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(ad.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(ad.description)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${ad.city} • ${ad.authorName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
