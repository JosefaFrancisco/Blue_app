package com.example.blue_app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val allItems = listOf(
        BottomNavItem("home", Icons.Default.Home, "Inicio"),
        BottomNavItem("notifications", Icons.Default.Notifications, "Notificaciones"),
        BottomNavItem("profile", Icons.Default.Person, "Perfil"),
        BottomNavItem("settings", Icons.Default.Settings, "Ajustes")
    )

    NavigationBar(
        containerColor = Color.White // Fondo blanco
    ) {
        allItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (currentDestination?.hierarchy?.any { it.route == item.route } == true) {
                            MaterialTheme.colorScheme.primary // Icono azul cuando está seleccionado
                        } else {
                            Color.Gray // Icono gris cuando no está seleccionado
                        }
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (currentDestination?.hierarchy?.any { it.route == item.route } == true) {
                            MaterialTheme.colorScheme.primary // Texto azul cuando está seleccionado
                        } else {
                            Color.Black // Texto negro cuando no está seleccionado
                        }
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)