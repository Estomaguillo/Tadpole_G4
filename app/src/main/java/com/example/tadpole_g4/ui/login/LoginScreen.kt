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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tadpole_g4.viewmodel.UserViewModel
import com.example.tadpole_g4.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    // ================================================================
    // ESTADOS OBSERVADOS DESDE EL VIEWMODEL
    // ================================================================
    val username by userViewModel.username
    val password by userViewModel.password

    // ================================================================
    // CONTROL DE VISIBILIDAD DE CONTRASEÑA
    // (permite mostrar/ocultar los caracteres)
    // ================================================================
    var passwordVisible by remember { mutableStateOf(false) }

    // ================================================================
    // ESTADO PARA MENSAJES DE ERROR
    // ================================================================
    var error by remember { mutableStateOf<String?>(null) }

    // ================================================================
    // SCROLL VERTICAL PARA AJUSTE EN PANTALLAS PEQUEÑAS
    // ================================================================
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
            // ================================================================
            // LOGO SUPERIOR
            // ================================================================
            Image(
                painter = painterResource(id = R.drawable.logo_login),
                contentDescription = "Logo de inicio de sesión",
                modifier = Modifier
                    .size(220.dp)
                    .padding(top = 32.dp, bottom = 48.dp)
            )

            // ================================================================
            // CAMPO DE USUARIO
            // ================================================================
            OutlinedTextField(
                value = username,
                onValueChange = {
                    userViewModel.onUsernameChange(it)
                    error = null // Limpiar error al escribir
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ================================================================
            // CAMPO DE CONTRASEÑA CON VISIBILIDAD CONTROLADA
            // ================================================================
            OutlinedTextField(
                value = password,
                onValueChange = {
                    userViewModel.onPasswordChange(it)
                    error = null // Limpiar error al escribir
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // Forzar teclado tipo contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                // Oculta los caracteres salvo que passwordVisible = true
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

                // ================================================================
                // ÍCONO DE MOSTRAR / OCULTAR CONTRASEÑA
                // ================================================================
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible)
                        "Ocultar contraseña"
                    else
                        "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ================================================================
            // BOTÓN DE ACCESO
            // ================================================================
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
                            // Intento de inicio de sesión a través del ViewModel
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

            // ================================================================
            // MENSAJE DE ERROR (SI EXISTE)
            // ================================================================
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
