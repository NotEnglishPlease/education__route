package com.example.educationroute.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.educationroute.R
import com.example.educationroute.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController?) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val viewModel: MainViewModel = viewModel(
        key = "main_view_model",
        factory = MainViewModel.Factory
    )
    val clientId by viewModel.currentClientId.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        Log.d("MainScreen", "MainScreen запущен")
        Log.d("MainScreen", "Текущий clientId: $clientId")
    }

    LaunchedEffect(clientId) {
        Log.d("MainScreen", "clientId изменился: $clientId")
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.values().forEach { item ->
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
            startDestination = BottomNavItem.AvailableCourses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.AvailableCourses.route) {
                when (val currentClientId = clientId) {
                    null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ошибка: ID клиента не найден")
                        }
                    }
                    else -> {
                        AvailableCoursesScreen(
                            navController = navController!!,
                            clientId = currentClientId
                        )
                    }
                }
            }
            composable(BottomNavItem.MyCourses.route) {
                when (val currentClientId = clientId) {
                    null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ошибка: ID клиента не найден")
                        }
                    }
                    else -> {
                        MyCoursesScreen(clientId = currentClientId)
                    }
                }
            }
            composable(BottomNavItem.Payment.route) {
                PaymentScreen(navController = bottomNavController)
            }
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val iconResId: Int,
    val label: String
) {
    object AvailableCourses : BottomNavItem(
        route = "available_courses",
        iconResId = R.drawable.ic_available_courses,
        label = "Доступные"
    )

    object MyCourses : BottomNavItem(
        route = "my_courses",
        iconResId = R.drawable.ic_my_courses,
        label = "Мои курсы"
    )

    object Payment : BottomNavItem(
        route = "payment",
        iconResId = R.drawable.ic_payment,
        label = "Оплата"
    )

    companion object {
        fun values(): List<BottomNavItem> = listOf(
            AvailableCourses,
            MyCourses,
            Payment
        )
    }
}