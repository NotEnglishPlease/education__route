package com.example.educationroute.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.educationroute.data.Course
import com.example.educationroute.ui.screens.*
import com.example.educationroute.viewmodel.RegisterViewModel

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
    }
}