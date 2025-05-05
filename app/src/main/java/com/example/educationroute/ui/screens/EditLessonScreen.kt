package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.data.EditLesson
import com.example.educationroute.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLessonScreen(navController: NavController, lesson: EditLesson) {
    var subject by remember { mutableStateOf(lesson.subject) }
    var ageGroup by remember { mutableStateOf(lesson.ageGroup) }
    var weekDay by remember { mutableStateOf(lesson.weekDay) }
    var startTime by remember { mutableStateOf(lesson.startTime) }
    var endTime by remember { mutableStateOf(lesson.endTime) }
    var tutor by remember { mutableStateOf(lesson.tutor) }
    var students by remember { mutableStateOf(lesson.students) }

    var showAddStudentDialog by remember { mutableStateOf(false) }
    var newStudentName by remember { mutableStateOf("") }

    if (showAddStudentDialog) {
        AlertDialog(
            onDismissRequest = { showAddStudentDialog = false },
            title = { Text("Добавить ученика") },
            text = {
                OutlinedTextField(
                    value = newStudentName,
                    onValueChange = { newStudentName = it },
                    label = { Text("Имя ученика") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newStudentName.isNotBlank()) {
                            val newId = (students.maxOfOrNull { it.id } ?: 0) + 1
                            students = students + Student(
                                id = newId,
                                name = newStudentName,
                                isSelected = true
                            )
                            newStudentName = ""
                            showAddStudentDialog = false
                        }
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStudentDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование занятия") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // TODO: Сохранить изменения
                            navController.popBackStack()
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
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
            item {
                Text("Основная информация", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Предмет") }
                )
                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { ageGroup = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Возрастная группа") }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Расписание", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = weekDay,
                    onValueChange = { weekDay = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("День недели") }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Начало") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Конец") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Преподаватель", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = tutor,
                    onValueChange = { tutor = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("ФИО преподавателя") }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ученики", style = MaterialTheme.typography.titleMedium)
                    Row {
                        IconButton(
                            onClick = {
                                showAddStudentDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Добавить ученика"
                            )
                        }
                    }
                }
                Column {
                    students.forEach { student ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = student.name,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    students = students.filter { it.id != student.id }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Удалить ученика"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 