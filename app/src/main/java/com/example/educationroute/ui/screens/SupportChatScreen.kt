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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.educationroute.R
import com.example.educationroute.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SupportChatScreen() {
    var messageText by remember { mutableStateOf("") }
    var showEmptyMessageError by remember { mutableStateOf(false) } // Флаг ошибки
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок чата
        Text(
            text = stringResource(R.string.support_chat),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            modifier = Modifier.fillMaxWidth(),
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

@Composable
fun MessageBubble(message: ChatMessage) {
    val bubbleColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (message.isFromUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = message.formattedTime(),
                    color = textColor.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}