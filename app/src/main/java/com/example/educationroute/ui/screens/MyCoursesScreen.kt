package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationroute.data.EnrolledCourse
import com.example.educationroute.ui.components.EnrolledCourseCard
import com.example.educationroute.viewmodel.CourseViewModel

@Composable
fun MyCoursesScreen() {
    val viewModel: CourseViewModel = viewModel()
    val courses = viewModel.enrolledCourses
    var showDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<EnrolledCourse?>(null) }

    if (showDialog) {
        selectedCourse?.let { course ->
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Сведения о занятии") },
                text = {
                    Column {
                        Text("Тема: ${course.currentTopic}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Оценка: ${course.grade ?: "Нет оценки"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Домашнее задание: ${course.homework ?: "Нет задания"}")
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Закрыть")
                    }
                }
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses) { course ->
            EnrolledCourseCard(
                course = course,
                onDetailsClick = {
                    selectedCourse = course
                    showDialog = true
                },
                onUnenrollClick = { viewModel.unenrollFromCourse(course.id) }
            )
        }
    }
}