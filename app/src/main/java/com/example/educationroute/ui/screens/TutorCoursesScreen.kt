package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.data.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCoursesScreen(navController: NavController) {
    // Временные данные для демонстрации
    val courses = remember {
        listOf(
            Course(
                id = 1,
                subject = "Математика",
                weekDay = "Понедельник",
                time = "15:00-16:30",
                ageGroup = "7-9 лет",
                teacher = "Иванова А.П.",
                studentsCount = 12
            ),
            Course(
                id = 2,
                subject = "Физика",
                weekDay = "Среда",
                time = "16:00-17:30",
                ageGroup = "10-12 лет",
                teacher = "Иванова А.П.",
                studentsCount = 8
            ),
            Course(
                id = 3,
                subject = "Информатика",
                weekDay = "Пятница",
                time = "17:00-18:30",
                ageGroup = "13-15 лет",
                teacher = "Иванова А.П.",
                studentsCount = 10
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои курсы") },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = course.subject,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "День: ${course.weekDay}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Время: ${course.time}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Возрастная группа: ${course.ageGroup}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Количество учеников: ${course.studentsCount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { 
                                    navController.navigate("conduct_lesson/${course.id}") {
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Text("Провести")
                            }
                            Button(
                                onClick = { /* Отменить занятие */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Отменить")
                            }
                        }
                    }
                }
            }
        }
    }
} 