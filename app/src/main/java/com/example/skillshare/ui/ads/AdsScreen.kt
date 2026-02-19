package com.example.skillshare.ui.ads

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
@Composable
fun AdsScreen(
    viewModel: AdsViewModel,
    onCreateClick: () -> Unit
) {
    val ads by viewModel.ads.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = onCreateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать объявление")
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
fun AdItem(ad: AdEntity) {
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
