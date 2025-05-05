package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.educationroute.R

@Composable
fun AdminBottomNavigation(navController: NavController) {
    val items = listOf(
        AdminBottomNavItem(
            route = "admin_schedule",
            title = "Расписание",
            iconResId = R.drawable.ic_available_courses
        ),
        AdminBottomNavItem(
            route = "admin_chats",
            title = "Чаты",
            iconResId = R.drawable.ic_support_chat
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = item.iconResId), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

data class AdminBottomNavItem(
    val route: String,
    val title: String,
    val iconResId: Int
) 