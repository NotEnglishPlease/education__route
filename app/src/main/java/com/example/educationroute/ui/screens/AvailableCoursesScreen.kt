package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educationroute.data.Course
import com.example.educationroute.ui.components.CourseCard
import com.example.educationroute.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableCoursesScreen(
    navController: NavController,
    clientId: Int
) {
    val viewModel: CourseViewModel = viewModel(
        key = "course_view_model",
        factory = CourseViewModel.Factory
    )
    val courses = viewModel.filteredCourses
    val searchQuery = viewModel.searchQuery
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(clientId) {
        try {
            isLoading.value = true
            error.value = null
            viewModel.loadAvailableCourses(clientId)
        } catch (e: Exception) {
            error.value = "Ошибка загрузки курсов: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
        SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                onSearch = { viewModel.setSearchQuery(it) },
            active = false,
            onActiveChange = {},
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = {
                Text("Поиск курсов", color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {}
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error.value != null -> {
                    Text(
                        text = error.value ?: "Произошла ошибка",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                courses.isEmpty() -> {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "Нет доступных курсов для вашего возраста"
                        } else {
                            "Курсы не найдены"
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                CourseCard(
                    course = course,
                    onEnrollClick = {
                        viewModel.enrollToCourse(
                            clientId = clientId,
                            lessonId = course.id,
                            onSuccess = {
                                viewModel.removeCourse(course.id)
                            },
                            onError = { err ->
                                error.value = err
                            }
                        )
                    }
                )
                        }
                    }
                }
            }
        }
    }
}