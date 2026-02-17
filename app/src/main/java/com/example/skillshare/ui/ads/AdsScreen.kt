package com.example.skillshare.ui.ads

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdsScreen() {
    val ads = remember {
        listOf(
            Ad(1, "Урок английского", "Помогу подтянуть разговорный английский", "Москва", "Алексей"),
            Ad(2, "Ремонт компьютера", "Почищу ноутбук, переустановлю Windows", "Санкт-Петербург", "Иван"),
            Ad(3, "Дизайн логотипа", "Сделаю простой логотип", "Казань", "Мария")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(ads) { ad ->
            AdItem(ad)
            Spacer(modifier = Modifier.height(12.dp))
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
            Text(ad.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${ad.city} • ${ad.authorName}", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
