package com.example.skillshare.ui.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.skillshare.ui.auth.AuthViewModel

@Composable
fun AdsScreen(
    viewModel: AdsViewModel,
    authViewModel: AuthViewModel,
    onCreateClick: () -> Unit,
    onAdClick: (Long) -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val ads by viewModel.mainAds.collectAsState()
    LaunchedEffect(currentUser) {
        viewModel.setCurrentUser(currentUser?.id)
    }
    var searchText by remember { mutableStateOf("") }
    var cityText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.updateSearchQuery(it.ifBlank { null })
            },
            label = { Text("Поиск", color = Color.White) },
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
            value = cityText,
            onValueChange = {
                cityText = it
                viewModel.updateCityFilter(it.ifBlank { null })
            },
            label = { Text("Город", color = Color.White) },
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
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать объявление", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(ads) { ad ->
                AdItem(ad = ad, onClick = { onAdClick(ad.id) })
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF262628)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(ad.title, color = Color.White)

            Spacer(modifier = Modifier.height(4.dp))

            Text(ad.description, color = Color.LightGray)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${ad.city} • ${ad.authorName}",
                color = Color(0xFF0BB7F5)
            )
        }
    }
}