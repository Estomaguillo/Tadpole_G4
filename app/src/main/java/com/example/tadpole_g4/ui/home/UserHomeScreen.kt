package com.example.tadpole_g4.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tadpole_g4.R
import com.example.tadpole_g4.viewmodel.UserViewModel

/**
 * --------------------------------------------------------------------
 *  HOME EXCLUSIVO PARA USUARIOS REGULARES (NO ADMIN)
 *  Muestra mensaje de bienvenida con nombre del usuario
 *  Menú inferior con 2 opciones (Inicio / Configuración)
 *  Incluye logo como en la pantalla del Admin
 * --------------------------------------------------------------------
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(userViewModel: UserViewModel, navController: NavController) {

    // Usuario actualmente logueado desde el ViewModel
    val currentUser = userViewModel.currentUser

    // Pestaña seleccionada dentro del menú inferior (por defecto 0 = Inicio)
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¡Bienvenido, ${currentUser?.username ?: ""}!") },
                actions = {
                    TextButton(onClick = {
                        // Cierra sesión y vuelve al Login
                        userViewModel.clearLoginFields()
                        navController.navigate("login") {
                            popUpTo("userHome") { inclusive = true }
                        }
                    }) {
                        Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
                    label = { Text("Configuración") }
                )
            }
        }
    ) { padding ->

        // Cambia el contenido según la pestaña elegida
        when (selectedTab) {

            // Pantalla de inicio
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // LOGO como en Home del Admin
                    Image(
                        painter = painterResource(id = R.drawable.logo_home_usuario),
                        contentDescription = "Logo",
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Gracias por utilizar los servicios de Tadpole!",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Configuración básica
            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Configuración del usuario", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Aquí podrás agregar configuraciones personales más adelante.")
                }
            }
        }
    }
}
