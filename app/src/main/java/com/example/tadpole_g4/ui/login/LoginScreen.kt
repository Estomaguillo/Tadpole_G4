package com.example.tadpole_g4.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tadpole_g4.viewmodel.UserViewModel
import com.example.tadpole_g4.R

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // Estructura general
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // 🖼️ LOGO FIJO ARRIBA
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_login),
                contentDescription = "Logo de inicio de sesión",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
        }

        // 📋 FORMULARIO CENTRADO VERTICALMENTE EN LA PANTALLA
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (userViewModel.login(username, password)) {
                        onLoginSuccess()
                    } else {
                        error = "Usuario o contraseña incorrectos"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar")
            }

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
