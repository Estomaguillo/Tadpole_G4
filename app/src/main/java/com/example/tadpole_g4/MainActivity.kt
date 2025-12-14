package com.example.tadpole_g4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.tadpole_g4.data.AppDatabase
import com.example.tadpole_g4.repository.UserRepository
import com.example.tadpole_g4.viewmodel.UserViewModel
import com.example.tadpole_g4.viewmodel.UserViewModelFactory
import com.example.tadpole_g4.navigation.AppNavigation
import com.example.tadpole_g4.ui.theme.Tadpole_g4Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la base de datos
        val database = AppDatabase.getDatabase(this)
        val userDao = database.usuarioDao()

        // Crea el repositorio
        val repository = UserRepository(userDao)

        // Crea el ViewModel usando Factory
        val userViewModel: UserViewModel by viewModels {
            UserViewModelFactory(repository)
        }

        setContent {
            Tadpole_g4Theme {
                val navController = rememberNavController()

                // Navegación centralizada en AppNavigation.kt
                AppNavigation(
                    navController = navController,
                    userViewModel = userViewModel
                )
            }
        }
    }
}
