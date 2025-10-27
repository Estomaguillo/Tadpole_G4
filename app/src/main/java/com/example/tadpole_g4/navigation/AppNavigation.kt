package com.example.tadpole_g4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tadpole_g4.ui.home.HomeScreen
import com.example.tadpole_g4.ui.login.LoginScreen
import com.example.tadpole_g4.viewmodel.UserViewModel

// Rutas de navegación
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // evita volver al login con "atrás"
                    }
                }
            )
        }

        // Pantalla Home (CRUD)
        composable(Screen.Home.route) {
            HomeScreen(
                userViewModel = userViewModel,
                navController = navController // ahora pasamos el controlador al Home
            )
        }
    }
}
