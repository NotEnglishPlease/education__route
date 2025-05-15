package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.R
import com.example.educationroute.model.LessonDTO
import com.example.educationroute.model.EmployeeDTO
import com.example.educationroute.network.RetrofitInstance

// Функция для определения порядка дней недели
private fun getDayOrder(day: String): Int {
    return when (day) {
        "Понедельник" -> 1
        "Вторник" -> 2
        "Среда" -> 3
        "Четверг" -> 4
        "Пятница" -> 5
        "Суббота" -> 6
        else -> 7
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleScreen(navController: NavController) {
    val lessonsState = produceState<List<LessonDTO>>(initialValue = emptyList()) {
        value = try {
            RetrofitInstance.api.getLessons().sortedWith(compareBy(
                { getDayOrder(it.weekDay) },
                { it.time.split("-")[0] }
            ))
        } catch (e: Exception) {
            emptyList()
        }
    }

    var employees by remember { mutableStateOf<List<EmployeeDTO>>(emptyList()) }
    LaunchedEffect(Unit) {
        try {
            employees = RetrofitInstance.api.getEmployees()
        } catch (_: Exception) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расписание") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("edit_lesson/new") {
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить занятие")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lessonsState.value) { lesson ->
                val employeeName = employees.firstOrNull { it.id == lesson.employeeId }?.name ?: "Неизвестно"
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
                            text = lesson.subject,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${lesson.weekDay}, ${lesson.time}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Возрастная группа: ${lesson.ageLevel}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Преподаватель: $employeeName",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                navController.navigate("edit_lesson/${lesson.id}") {
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
        }
    }
}
