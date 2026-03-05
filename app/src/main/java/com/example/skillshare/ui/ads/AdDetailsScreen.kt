package com.example.skillshare.ui.ads

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    // SnackbarHost для уведомлений
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope() // ⚡ для запуска корутин из onClick

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        ad?.let { adItem ->

            val isOwner = currentUser?.id == adItem.userId

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Text(
                    text = adItem.title,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(adItem.description)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "${adItem.city} • ${adItem.authorName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isOwner) {
                    Button(
                        onClick = onEdit,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Редактировать")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            adsViewModel.deleteAd(adItem)
                            onBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Удалить")
                    }

                } else {
                    Button(
                        onClick = {
                            currentUser?.let { user ->
                                // Предлагаем обмен
                                exchangeViewModel.proposeExchange(
                                    adId = adItem.id,
                                    fromUserId = user.id,
                                    toUserId = adItem.userId
                                )

                                // Показываем Snackbar через coroutineScope
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Обмен предложен!")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Предложить обмен")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onBack) {
                    Text("Назад")
                }
            }
        }
    }
}