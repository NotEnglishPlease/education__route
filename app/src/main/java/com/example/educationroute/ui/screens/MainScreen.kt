package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.educationroute.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController?) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                AvailableCoursesScreen(navController!!)
            }
            composable(BottomNavItem.MyCourses.route) {
                MyCoursesScreen()
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