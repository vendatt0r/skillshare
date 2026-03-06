package com.example.skillshare.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import com.example.skillshare.ui.auth.AuthViewModel

@Composable
fun ExchangeChatScreen(
    exchangeId: Long,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val messages by chatViewModel.getMessages(exchangeId).collectAsState(initial = emptyList())
    var text by remember { mutableStateOf("") }

    // Получаем высоту статусбара
    val statusBarHeight = with(LocalDensity.current) {
        val view = LocalView.current
        val insets = ViewCompat.getRootWindowInsets(view)
        (insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0).toDp()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔹 Верхняя панель с заголовком с учетом статусбара
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarHeight, start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Чат", style = MaterialTheme.typography.titleMedium)
        }
        Divider()

        // 🔹 Сообщения с прокруткой
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                val isMe = message.senderId == currentUser?.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        modifier = Modifier.padding(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = message.message,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Divider()

        // 🔹 Поле ввода и кнопка "Send"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    currentUser?.id?.let { userId ->
                        if (text.isNotBlank()) {
                            chatViewModel.sendMessage(exchangeId, userId, text)
                            text = ""
                        }
                    }
                }
            ) {
                Text("Send")
            }
        }

        // 🔹 Кнопка выхода под полем ввода
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Выйти") // ✅ чисто текст
        }
    }
}