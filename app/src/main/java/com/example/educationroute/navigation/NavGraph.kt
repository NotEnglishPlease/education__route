package com.example.educationroute.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    }
}