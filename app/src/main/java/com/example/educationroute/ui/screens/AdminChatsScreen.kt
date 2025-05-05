package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ChatInfo(
    val parentName: String,
    val studentName: String,
    val paidLessons: Int,
    val lessonsPerWeek: Int
)

@Composable
fun AdminChatsScreen(navController: NavController) {
    // Временные данные для демонстрации
    val chats = remember {
        listOf(
            ChatInfo(
                parentName = "Иванов Иван",
                studentName = "Иванов Петр",
                paidLessons = 8,
                lessonsPerWeek = 2
            ),
            ChatInfo(
                parentName = "Петрова Мария",
                studentName = "Петров Алексей",
                paidLessons = 12,
                lessonsPerWeek = 3
            ),
            ChatInfo(
                parentName = "Сидоров Сергей",
                studentName = "Сидорова Анна",
                paidLessons = 4,
                lessonsPerWeek = 1
            )
        )
    }

    Scaffold(
        bottomBar = { AdminBottomNavigation(navController = navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chats) { chat ->
                    ChatCard(chatInfo = chat, navController = navController)
                }
            }
        }
    }
}

@Composable
fun ChatCard(chatInfo: ChatInfo, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Родитель: ${chatInfo.parentName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Ученик: ${chatInfo.studentName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Оплачено занятий: ${chatInfo.paidLessons}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Занятий в неделю: ${chatInfo.lessonsPerWeek}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(
                    onClick = {
                        navController.navigate("client_chat/${chatInfo.parentName}/${chatInfo.studentName}/${chatInfo.paidLessons}/${chatInfo.lessonsPerWeek}")
                    }
                ) {
                    Text("Связаться")
                }
            }
        }
    }
} 