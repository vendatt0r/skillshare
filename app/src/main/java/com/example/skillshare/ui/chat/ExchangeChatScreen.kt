package com.example.skillshare.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    val statusBarHeight = with(LocalDensity.current) {
        val view = LocalView.current
        val insets = ViewCompat.getRootWindowInsets(view)
        (insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0).toDp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A13))
    ) {

        // 🔹 Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarHeight, start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Чат",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }

        Divider(color = Color.DarkGray)

        // 🔹 Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            items(messages) { message ->
                val isMe = message.senderId == currentUser?.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe)
                                Color(0xFF3896CC)
                            else
                                Color(0xFF9BAAB1)
                        )
                    ) {
                        Text(
                            text = message.message,
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Divider(color = Color.DarkGray)

        // 🔹 Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1C1C1E),
                    unfocusedContainerColor = Color(0xFF1C1C1E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
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
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E676)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Send", color = Color.Black)
            }
        }

        // 🔹 Back button
        TextButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Назад", color = Color(0xFF0BB7F5))
        }
    }
}