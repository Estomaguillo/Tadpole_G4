package com.example.tadpole_g4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tadpole_g4.ui.home.HomeScreen
import com.example.tadpole_g4.ui.home.UserHomeScreen
import com.example.tadpole_g4.ui.login.LoginScreen
import com.example.tadpole_g4.viewmodel.UserViewModel

// Definimos las rutas de la app
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object HomeAdmin : Screen("home_admin")
    object HomeUser : Screen("home_user")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    // Controlador de navegación principal
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // --- Pantalla de Login ---
        composable(Screen.Login.route) {
            // Pasamos el viewModel y el callback que determina el tipo de usuario
            LoginScreen(userViewModel = userViewModel) { userType ->
                if (userType == "admin") {
                    // Si es administrador, va al Home de Admin
                    navController.navigate(Screen.HomeAdmin.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // Limpia el back stack
                    }
                } else {
                    // Si es usuario normal, va al Home de Usuario
                    navController.navigate(Screen.HomeUser.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }

        // --- Home del Administrador ---
        composable(Screen.HomeAdmin.route) {
            HomeScreen(
                userViewModel = userViewModel,
                navController = navController
            )
        }

        // --- Home del Usuario Normal ---
        composable(Screen.HomeUser.route) {
            UserHomeScreen(
                userViewModel = userViewModel,
                navController = navController
            )
        }
    }
}
