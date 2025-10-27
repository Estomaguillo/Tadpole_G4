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

    // VARIABLES

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //variable para mostrar mensajes de error de validación
    var error by remember { mutableStateOf<String?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        //LOGO FIJO ARRIBA
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

        // FORMULARIO CENTRADO
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo usuario
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    error = null // limpiar error al escribir
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    error = null // 💬 NUEVO: limpiar error al escribir
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))


            // BOTÓN DE ACCESO CON VALIDACIONES

            Button(
                onClick = {
                    // Validaciones antes de intentar el login
                    when {
                        username.isBlank() -> {
                            error = "El campo de usuario no puede estar vacío"
                        }
                        password.isBlank() -> {
                            error = "La contraseña no puede estar vacía"
                        }
                        password.length < 4 -> {
                            error = "La contraseña debe tener al menos 4 caracteres"
                        }
                        else -> {
                            // Intentar login solo si pasa validaciones
                            if (userViewModel.login(username, password)) {
                                error = null
                                onLoginSuccess()
                            } else {
                                error = "Usuario o contraseña incorrectos"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aceptar")
            }

            // Mostrar mensaje de error si existe
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
