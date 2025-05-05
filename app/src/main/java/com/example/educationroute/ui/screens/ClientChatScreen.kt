package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.educationroute.R
import com.example.educationroute.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClientChatScreen(
    parentName: String,
    studentName: String,
    paidLessons: Int,
    lessonsPerWeek: Int
) {
    var messageText by remember { mutableStateOf("") }
    var showEmptyMessageError by remember { mutableStateOf(false) }
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                id = 1,
                text = "Здравствуйте! Чем могу помочь?",
                isFromUser = false,
                timestamp = Date(System.currentTimeMillis() - 3600000)
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Информация о клиенте
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Родитель: $parentName",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Ученик: $studentName",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Оплачено: $paidLessons",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "В неделю: $lessonsPerWeek",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Список сообщений
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.reversed()) { message ->
                MessageBubble(message = message)
            }
        }

        // Сообщение об ошибке
        if (showEmptyMessageError) {
            Text(
                text = stringResource(R.string.empty_message_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
        }

        // Поле ввода сообщения
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = {
                    messageText = it
                    if (showEmptyMessageError && it.isNotBlank()) {
                        showEmptyMessageError = false
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.type_message)) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                isError = showEmptyMessageError,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    errorContainerColor = MaterialTheme.colorScheme.errorContainer
                )
            )

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        messages.add(
                            ChatMessage(
                                id = messages.size + 1,
                                text = messageText,
                                isFromUser = true,
                                timestamp = Date()
                            )
                        )
                        messageText = ""
                        showEmptyMessageError = false
                    } else {
                        showEmptyMessageError = true
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = stringResource(R.string.send_message),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
} 