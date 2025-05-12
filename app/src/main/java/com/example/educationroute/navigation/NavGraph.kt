package com.example.educationroute.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.educationroute.data.Course
import com.example.educationroute.data.EditLesson
import com.example.educationroute.data.Lesson
import com.example.educationroute.data.Student
import com.example.educationroute.data.StudentGrade
import com.example.educationroute.ui.screens.*
import com.example.educationroute.viewmodel.RegisterViewModel
import com.example.educationroute.model.LessonDTO
import com.example.educationroute.network.RetrofitInstance
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val registerViewModel = remember { RegisterViewModel() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("filters") {
            FilterScreen(navController)
        }
        composable("tutor_courses") {
            TutorCoursesScreen(navController)
        }
        composable("admin_schedule") {
            AdminScheduleScreen(navController = navController)
        }
        composable(
            route = "conduct_lesson/{courseId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            // TODO: Получить курс по ID из репозитория
            val course = Course(
                id = courseId,
                subject = "Математика",
                weekDay = "Понедельник",
                time = "15:00-16:30",
                ageGroup = "7-9 лет",
                teacher = "Иванова А.П.",
                studentsCount = 12
            )
            ConductLessonScreen(navController, course)
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
                } catch (_: Exception) {}
                isLoading.value = false
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
                
                EditLessonScreen(navController = navController, lesson = editLesson)
            }
        }
    }
}