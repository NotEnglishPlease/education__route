package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educationroute.R
import com.example.educationroute.data.Course
import com.example.educationroute.ui.components.CourseCard
import com.example.educationroute.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableCoursesScreen(navController: NavController) {
    val viewModel: CourseViewModel = viewModel()
    val courses = viewModel.availableCourses
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar с иконками
        SearchBar(
            query = searchQuery.value,
            onQueryChange = { searchQuery.value = it },
            onSearch = { /* Логика поиска */ },
            active = false,
            onActiveChange = {},
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                IconButton(onClick = { navController.navigate("filters") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "Фильтры",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            placeholder = {
                Text("Поиск курсов", color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {}

        // Список курсов
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                CourseCard(
                    course = course,
                    onEnrollClick = { viewModel.enrollToCourse(course.id) }
                )
            }
        }
    }
}