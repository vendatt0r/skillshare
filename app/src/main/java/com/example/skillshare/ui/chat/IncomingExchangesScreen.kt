package com.example.skillshare.ui.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        currentUser?.id?.let { exchangeViewModel.loadUserExchanges(it) }
    }

    LazyColumn {
        // Показываем все PENDING, ACCEPTED, COMPLETING, COMPLETED; CANCELLED не показываем
        val filteredExchanges = exchanges.filter {
            (it.toUserId == currentUser?.id || it.fromUserId == currentUser?.id) &&
                    it.status != ExchangeStatus.CANCELLED
        }

        items(filteredExchanges) { exchange ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // 🔹 Статус с цветной меткой
                    val (statusText, statusColor) = when (exchange.status) {
                        ExchangeStatus.PENDING -> "Pending" to Color.Yellow
                        ExchangeStatus.ACCEPTED -> "Accepted" to Color.Green
                        ExchangeStatus.COMPLETING -> "Ожидает подтверждения" to Color.Cyan
                        ExchangeStatus.COMPLETED -> "Completed" to Color.Gray
                        else -> "" to Color.Transparent
                    }

                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text(
                            text = statusText,
                            color = Color.White,
                            modifier = Modifier
                                .background(statusColor)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Обмен с пользователем ${if (exchange.fromUserId == currentUser?.id) exchange.toUserId else exchange.fromUserId}"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 🔹 Кнопки действий
                    when (exchange.status) {
                        ExchangeStatus.PENDING -> {
                            if (exchange.toUserId == currentUser?.id) {
                                Row {
                                    Button(onClick = {
                                        currentUser?.let { user ->
                                            exchangeViewModel.acceptExchange(exchange, user.id)
                                        }
                                        onOpenChat(exchange.id)
                                    }) { Text("Принять") }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(onClick = {
                                        currentUser?.let { user ->
                                            exchangeViewModel.cancelExchange(exchange, user.id)
                                        }
                                    }) { Text("Отклонить") }
                                }
                            }
                        }

                        ExchangeStatus.ACCEPTED, ExchangeStatus.COMPLETING -> {
                            Row {
                                Button(onClick = { onOpenChat(exchange.id) }) { Text("Открыть чат") }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(onClick = {
                                    currentUser?.let { user ->
                                        exchangeViewModel.requestCompleteExchange(exchange, user.id)
                                    }
                                }) { Text("Завершить обмен") }
                            }
                        }

                        ExchangeStatus.COMPLETED -> {
                            Text("Обмен завершён")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}