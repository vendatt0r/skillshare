package com.example.skillshare.ui.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

    LaunchedEffect(currentUser?.id) {
        currentUser?.id?.let { exchangeViewModel.loadUserExchanges(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
            .padding(vertical = 8.dp)
    ) {

        val filteredExchanges = exchanges.filter {
            (it.toUserId == currentUser?.id || it.fromUserId == currentUser?.id) &&
                    it.status != ExchangeStatus.CANCELLED
        }

        items(filteredExchanges) { exchange ->

            val alreadyConfirmed =
                (currentUser?.id == exchange.fromUserId && exchange.fromUserConfirmed) ||
                        (currentUser?.id == exchange.toUserId && exchange.toUserConfirmed)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    val (statusText, statusColor) = when (exchange.status) {
                        ExchangeStatus.PENDING -> "Ожидает ответа" to Color.Yellow
                        ExchangeStatus.ACCEPTED -> "Принят" to Color(0xFF00E676)
                        ExchangeStatus.COMPLETING -> "Ожидает подтверждения" to Color.Cyan
                        ExchangeStatus.COMPLETED -> "Завершён" to Color.Gray
                        else -> "" to Color.Transparent
                    }

                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Text(
                            text = statusText,
                            color = Color.Black,
                            modifier = Modifier
                                .background(statusColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Обмен с пользователем ${
                                if (exchange.fromUserId == currentUser?.id)
                                    exchange.toUserId
                                else
                                    exchange.fromUserId
                            }",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Предложение:",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = exchange.offerText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    when (exchange.status) {

                        ExchangeStatus.PENDING -> {
                            if (exchange.toUserId == currentUser?.id) {
                                Row {
                                    Button(
                                        onClick = {
                                            currentUser?.let { user ->
                                                exchangeViewModel.acceptExchange(exchange, user.id)
                                            }
                                            onOpenChat(exchange.id)
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Принять", color = Color.Black)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            currentUser?.let { user ->
                                                exchangeViewModel.cancelExchange(exchange, user.id)
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0BB7F5)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Отклонить", color = Color.Black)
                                    }
                                }
                            }
                        }

                        ExchangeStatus.ACCEPTED, ExchangeStatus.COMPLETING -> {
                            Row {
                                Button(
                                    onClick = { onOpenChat(exchange.id) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Открыть чат", color = Color.Black)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        currentUser?.let { user ->
                                            exchangeViewModel.requestCompleteExchange(exchange, user.id)
                                        }
                                    },
                                    enabled = !alreadyConfirmed, // 👈 фикс
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0BB7F5)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        if (alreadyConfirmed)
                                            "Ожидание второго"
                                        else
                                            "Завершить обмен",
                                        color = Color.Black
                                    )
                                }
                            }

                            // 👇 Подсказка
                            if (exchange.status == ExchangeStatus.COMPLETING) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ожидается подтверждение второго участника",
                                    color = Color.Cyan,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        ExchangeStatus.COMPLETED -> {
                            Text(
                                "Обмен завершён",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}