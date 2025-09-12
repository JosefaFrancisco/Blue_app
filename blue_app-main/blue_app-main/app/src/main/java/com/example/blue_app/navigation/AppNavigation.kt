package com.example.blue_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blue_app.Article
import com.example.blue_app.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "register",
        modifier = modifier
    ) {

        // Pantallas de autenticación
        composable("register") {
            RegisterScreen(
                onRegisterClick = {
                    // Aquí luego pondremos lógica para registrar con API
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = false }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    // Aquí luego pondremos lógica para iniciar sesión con API
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = false }
                    }
                },
                onForgotPasswordClick = {
                    // Podríamos añadir pantalla para recuperar contraseña más adelante
                }
            )
        }

        composable("home") {
            HomePage(navController = navController)
        }

        composable("notifications") { NotificationPage() }
        composable("profile") { ProfilePage() }
        composable("settings") { SettingPage() }

        composable("add") {
            AddArticlePage(
                onArticleAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() } // Cambiado para ser consistente
            )
        }

        composable("edit") {
            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            if (article != null) {
                EditArticlePage(
                    navController = navController,
                    article = article,
                    onBack = { navController.popBackStack() } // Añadido parámetro onBack
                )
            }
        }

        composable("detail") {
            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            if (article != null) {
                DetailArticlePage(
                    article = article,
                    onBack = { navController.popBackStack() } // Añadido parámetro onBack
                )
            }
        }
    }
}