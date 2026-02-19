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
    onCreateClick: () -> Unit,
    onEditClick: (Long) -> Unit // id объявления для редактирования
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
                AdItem(
                    ad = ad,
                    onDelete = { viewModel.deleteAd(ad) },
                    onEdit = { onEditClick(ad.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AdItem(
    ad: AdEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Удалить")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать")
            }
        }
    }
}
