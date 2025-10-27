package com.example.tadpole_g4.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    // Variables observadas desde el ViewModel
    val username by userViewModel.username
    val password by userViewModel.password

    // Variable local para mostrar mensajes de error
    var error by remember { mutableStateOf<String?>(null) }

    // Scroll vertical para evitar recortes o solapamientos
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LOGO SUPERIOR
            Image(
                painter = painterResource(id = R.drawable.logo_login),
                contentDescription = "Logo de inicio de sesión",
                modifier = Modifier
                    .size(220.dp)
                    .padding(top = 32.dp, bottom = 48.dp)
            )

            // FORMULARIO DE LOGIN
            OutlinedTextField(
                value = username,
                onValueChange = {
                    userViewModel.onUsernameChange(it)
                    error = null
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    userViewModel.onPasswordChange(it)
                    error = null
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE ACCESO
            Button(
                onClick = {
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
                            if (userViewModel.login(username, password)) {
                                error = null
                                onLoginSuccess()
                            } else {
                                error = "Usuario o contraseña incorrectos"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Aceptar")
            }

            // MENSAJE DE ERROR
            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}


