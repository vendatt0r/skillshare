package com.example.skillshare.ui.ads

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.skillshare.ui.auth.AuthViewModel
import com.example.skillshare.ui.exchange.ExchangeViewModel
import kotlinx.coroutines.launch

@Composable
fun AdDetailsScreen(
    adsViewModel: AdsViewModel,
    authViewModel: AuthViewModel,
    exchangeViewModel: ExchangeViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val ad by adsViewModel.selectedAd.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color(0xFF070A13),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        ad?.let { adItem ->

            val isOwner = currentUser?.id == adItem.userId

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            ) {

                // 🔹 Заголовок объявления
                Text(
                    text = adItem.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Описание
                Text(
                    text = adItem.description,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Информация об авторе и городе
                Text(
                    text = "${adItem.city} • ${adItem.authorName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF00E676)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isOwner) {
                    // 🔹 Кнопка редактирования
                    Button(
                        onClick = onEdit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Редактировать", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 🔹 Кнопка удаления
                    Button(
                        onClick = {
                            adsViewModel.deleteAd(adItem)
                            onBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Удалить", color = Color.White)
                    }

                } else {
                    // 🔹 Кнопка предложения обмена
                    Button(
                        onClick = {
                            currentUser?.let { user ->
                                exchangeViewModel.proposeExchange(
                                    adId = adItem.id,
                                    fromUserId = user.id,
                                    toUserId = adItem.userId
                                )

                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Обмен предложен!")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Предложить обмен", color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Кнопка назад
                TextButton(onClick = onBack) {
                    Text("Назад", color = Color(0xFF0BB7F5))
                }
            }
        }
    }
}