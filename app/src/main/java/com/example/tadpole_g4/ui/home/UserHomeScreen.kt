package com.example.tadpole_g4.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tadpole_g4.navigation.Screen
import com.example.tadpole_g4.viewmodel.UserViewModel

@Composable
fun UserHomeScreen(
    userViewModel: UserViewModel,
    navController: NavController
) {
    val user = userViewModel.currentUser

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Bienvenido Usuario",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Nombre: ${user?.pnom_usu ?: "Desconocido"} ${user?.appaterno_usu ?: ""}")
            Text("RUT: ${user?.rut_usu ?: 0}")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    userViewModel.clearLoginFields()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.HomeUser.route) { inclusive = true }
                    }
                }
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}