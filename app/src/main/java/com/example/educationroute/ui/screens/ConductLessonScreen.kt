package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.data.Course
import com.example.educationroute.data.Lesson
import com.example.educationroute.data.StudentGrade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConductLessonScreen(navController: NavController, course: Course) {
    var topic by remember { mutableStateOf("") }
    var homework by remember { mutableStateOf("") }
    var studentGrades by remember { mutableStateOf(
        (1..course.studentsCount).associateWith { 
            StudentGrade(grade = null, isPresent = true)
        }
    )}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Проведение занятия") },
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
                            // TODO: Сохранить данные занятия
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
                Text(
                    text = course.subject,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${course.weekDay}, ${course.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Тема занятия", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Введите тему занятия") }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Оценки", style = MaterialTheme.typography.titleMedium)
                Column {
                    studentGrades.forEach { (studentId, grade) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = grade.isPresent,
                                onCheckedChange = { checked ->
                                    studentGrades = studentGrades.toMutableMap().apply {
                                        this[studentId] = StudentGrade(
                                            grade = if (checked) grade.grade else null,
                                            isPresent = checked
                                        )
                                    }
                                }
                            )
                            Text(
                                text = "Ученик $studentId",
                                modifier = Modifier.weight(1f)
                            )
                            if (grade.isPresent) {
                                OutlinedTextField(
                                    value = grade.grade?.toString() ?: "",
                                    onValueChange = { newValue ->
                                        val newGrade = newValue.toIntOrNull()
                                        studentGrades = studentGrades.toMutableMap().apply {
                                            this[studentId] = StudentGrade(
                                                grade = newGrade,
                                                isPresent = true
                                            )
                                        }
                                    },
                                    modifier = Modifier.width(80.dp),
                                    label = { Text("Балл", softWrap = false) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Домашнее задание", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = homework,
                    onValueChange = { homework = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Введите домашнее задание") },
                    minLines = 3
                )
            }
        }
    }
} 