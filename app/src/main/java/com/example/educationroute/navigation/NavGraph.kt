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
import com.example.educationroute.data.EditLesson
import com.example.educationroute.data.Lesson
import com.example.educationroute.data.Student
import com.example.educationroute.data.StudentGrade
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
        composable("admin_schedule") {
            AdminScheduleScreen(navController = navController)
        }
        composable("admin_chats") {
            AdminChatsScreen(navController = navController)
        }
        composable(
            route = "client_chat/{parentName}/{studentName}/{paidLessons}/{lessonsPerWeek}",
            arguments = listOf(
                navArgument("parentName") { type = NavType.StringType },
                navArgument("studentName") { type = NavType.StringType },
                navArgument("paidLessons") { type = NavType.IntType },
                navArgument("lessonsPerWeek") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val parentName = backStackEntry.arguments?.getString("parentName") ?: ""
            val studentName = backStackEntry.arguments?.getString("studentName") ?: ""
            val paidLessons = backStackEntry.arguments?.getInt("paidLessons") ?: 0
            val lessonsPerWeek = backStackEntry.arguments?.getInt("lessonsPerWeek") ?: 0
            ClientChatScreen(
                parentName = parentName,
                studentName = studentName,
                paidLessons = paidLessons,
                lessonsPerWeek = lessonsPerWeek
            )
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
            // TODO: Получить данные занятия по ID
            val lesson = EditLesson(
                subject = "Математика",
                ageGroup = "7-9 лет",
                weekDay = "Понедельник",
                startTime = "10:00",
                endTime = "11:30",
                tutor = "Иванов И.И.",
                students = listOf(
                    Student(1, "Петров Петр", true),
                    Student(2, "Иванов Иван", true),
                    Student(3, "Сидорова Анна", false)
                )
            )
            EditLessonScreen(navController = navController, lesson = lesson)
        }
    }
}