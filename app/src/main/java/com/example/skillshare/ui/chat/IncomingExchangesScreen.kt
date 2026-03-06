package com.example.skillshare.ui.exchange

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.skillshare.ui.auth.AuthViewModel

@Composable
fun IncomingExchangesScreen(
    exchangeViewModel: ExchangeViewModel,
    authViewModel: AuthViewModel,
    onOpenChat: (Long) -> Unit
) {

    val currentUser by authViewModel.currentUser.collectAsState()
    val exchanges by exchangeViewModel.userExchanges.collectAsState()

    // Загружаем обмены для текущего пользователя
    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let {
            exchangeViewModel.loadUserExchanges(it)
        }
    }

    LazyColumn {

        // Показываем все PENDING и ACCEPTED обмены, где пользователь участвует
        val filteredExchanges = exchanges.filter {
            (it.toUserId == currentUser?.id || it.fromUserId == currentUser?.id) &&
                    (it.status == ExchangeStatus.PENDING || it.status == ExchangeStatus.ACCEPTED)
        }

        items(filteredExchanges) { exchange ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = when (exchange.status) {
                            ExchangeStatus.PENDING -> "Предложение обмена"
                            ExchangeStatus.ACCEPTED -> "Активный обмен"
                            else -> ""
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (exchange.status == ExchangeStatus.PENDING && exchange.toUserId == currentUser?.id) {
                        Row {
                            Button(
                                onClick = {
                                    // Принять обмен
                                    currentUser?.let { user ->
                                        exchangeViewModel.acceptExchange(exchange, user.id)
                                    }
                                    // Открыть чат
                                    onOpenChat(exchange.id)
                                }
                            ) {
                                Text("Принять")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    currentUser?.let { user ->
                                        exchangeViewModel.cancelExchange(exchange, user.id)
                                    }
                                }
                            ) {
                                Text("Отклонить")
                            }
                        }
                    } else if (exchange.status == ExchangeStatus.ACCEPTED) {
                        Button(
                            onClick = { onOpenChat(exchange.id) }
                        ) {
                            Text("Открыть чат")
                        }
                    }
                }
            }
        }
    }
}