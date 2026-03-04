package com.example.skillshare.ui.ads

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.skillshare.ui.auth.AuthViewModel

@Composable
fun AdsScreen(
    viewModel: AdsViewModel,
    authViewModel: AuthViewModel,
    onCreateClick: () -> Unit,
    onAdClick: (Long) -> Unit
) {
    val ads by viewModel.ads.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var cityText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.updateSearchQuery(it.ifBlank { null })
            },
            label = { Text("Поиск по названию") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cityText,
            onValueChange = {
                cityText = it
                viewModel.updateCityFilter(it.ifBlank { null })
            },
            label = { Text("Фильтр по городу") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    onClick = { onAdClick(ad.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AdItem(
    ad: AdEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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