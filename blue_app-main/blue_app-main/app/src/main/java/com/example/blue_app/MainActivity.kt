package com.example.blue_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blue_app.navigation.AppNavigation
import com.example.blue_app.ui.theme.Blue_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Blue_appTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(
                    floatingActionButton = {
                        if (currentRoute in listOf("home", "notifications", "profile", "settings")) {
                            FloatingActionButton(
                                onClick = { navController.navigate("add") },
                                containerColor = Color(0xFFFF69B4), // Nuevo color
                                contentColor = Color.White,
                                modifier = Modifier
                                    .offset(y = 45.dp)
                                    .padding(4.dp), // Espacio para borde
                                shape = MaterialTheme.shapes.extraLarge // Bordes redondeados
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Agregar",
                                    modifier = Modifier.size(24.dp))
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    bottomBar = {
                        val noBottomBarRoutes = listOf("login", "register", "add")
                        if (currentRoute !in noBottomBarRoutes) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}