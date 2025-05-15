package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.educationroute.R
import com.example.educationroute.network.RetrofitInstance
import com.example.educationroute.model.LessonDTO
import com.example.educationroute.data.EditLesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(navController: NavController?) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                AdminBottomNavItem.values().forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = AdminBottomNavItem.Schedule.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AdminBottomNavItem.Schedule.route) {
                AdminScheduleScreen(navController = bottomNavController)
            }
            composable(AdminBottomNavItem.Clients.route) {
                AdminClientsScreen(navController = bottomNavController)
            }
            composable(
                route = "edit_lesson/{lessonId}",
                arguments = listOf(
                    navArgument("lessonId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val lessonId = backStackEntry.arguments?.getString("lessonId")
                val lesson = remember { mutableStateOf<LessonDTO?>(null) }
                val isLoading = remember { mutableStateOf(true) }
                
                LaunchedEffect(lessonId) {
                    isLoading.value = true
                    try {
                        if (lessonId != "new") {
                            lesson.value = RetrofitInstance.api.getLessonById(lessonId?.toIntOrNull() ?: 0)
                        }
                    } catch (e: Exception) {
                        println("Error loading lesson: ${e.message}")
                    } finally {
                        isLoading.value = false
                    }
                }
                
                if (isLoading.value) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val editLesson = if (lessonId == "new") {
                        EditLesson(
                            id = null,
                            subject = "",
                            ageGroup = "",
                            weekDay = "",
                            startTime = "",
                            endTime = "",
                            tutor = "",
                            students = emptyList()
                        )
                    } else {
                        val existingLesson = lesson.value
                        if (existingLesson != null) {
                            val timeParts = existingLesson.time.split("-")
                            EditLesson(
                                id = existingLesson.id,
                                subject = existingLesson.subject,
                                ageGroup = existingLesson.ageLevel.toString(),
                                weekDay = existingLesson.weekDay,
                                startTime = timeParts.getOrNull(0)?.replace(":", "") ?: "",
                                endTime = timeParts.getOrNull(1)?.replace(":", "") ?: "",
                                tutor = "", // Будет заполнено в EditLessonScreen
                                students = emptyList() // TODO: Добавить получение списка студентов
                            )
                        } else {
                            EditLesson(
                                id = lessonId?.toIntOrNull() ?: 0,
                                subject = "",
                                ageGroup = "",
                                weekDay = "",
                                startTime = "",
                                endTime = "",
                                tutor = "",
                                students = emptyList()
                            )
                        }
                    }
                    
                    EditLessonScreen(navController = bottomNavController, lesson = editLesson)
                }
            }
        }
    }
}

sealed class AdminBottomNavItem(
    val route: String,
    val iconResId: Int,
    val label: String
) {
    object Schedule : AdminBottomNavItem(
        route = "admin_schedule",
        iconResId = R.drawable.ic_available_courses,
        label = "Расписание"
    )

    object Clients : AdminBottomNavItem(
        route = "admin_clients",
        iconResId = R.drawable.ic_profile,
        label = "Клиенты"
    )

    companion object {
        fun values(): List<AdminBottomNavItem> = listOf(
            Schedule,
            Clients
        )
    }
} 