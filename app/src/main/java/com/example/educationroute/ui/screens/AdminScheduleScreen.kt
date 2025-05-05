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
import com.example.educationroute.data.Lesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleScreen(navController: NavController) {
    // TODO: Получить список всех курсов из репозитория
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
                subject = "Русский язык",
                weekDay = "Вторник",
                time = "14:00-15:30",
                ageGroup = "10-12 лет",
                teacher = "Петрова С.И.",
                studentsCount = 15
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
                items(courses) { course ->
                    LessonCard(course = course, navController = navController)
                }
            }
        }
    }
}

@Composable
fun LessonCard(course: Course, navController: NavController) {
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
                text = course.subject,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "${course.weekDay}, ${course.time}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Возрастная группа: ${course.ageGroup}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Преподаватель: ${course.teacher}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Количество учеников: ${course.studentsCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("edit_lesson/${course.id}") {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Редактировать")
            }
        }
    }
}

// Временные данные для демонстрации
private val lessons = listOf(
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