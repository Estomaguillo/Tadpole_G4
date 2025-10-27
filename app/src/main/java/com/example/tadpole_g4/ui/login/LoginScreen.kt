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
import kotlinx.coroutines.delay

/**
 * Pantalla de Login con animación de carga y validación.
 * Redirige a HomeScreen correspondiente según tipo de usuario.
 */
@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onLoginSuccess: (String) -> Unit // "admin" o "user"
) {
    val username by userViewModel.username
    val password by userViewModel.password

    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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
            // Logo superior
            Image(
                painter = painterResource(id = R.drawable.logo_login),
                contentDescription = "Logo de inicio de sesión",
                modifier = Modifier
                    .size(220.dp)
                    .padding(top = 32.dp, bottom = 48.dp)
            )

            // Campo de usuario
            OutlinedTextField(
                value = username,
                onValueChange = {
                    userViewModel.onUsernameChange(it)
                    error = null
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    userViewModel.onPasswordChange(it)
                    error = null
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Ocultar contraseña"
                            else
                                "Mostrar contraseña"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de acceso
            Button(
                onClick = {
                    when {
                        username.isBlank() -> error = "El campo de usuario no puede estar vacío"
                        password.isBlank() -> error = "La contraseña no puede estar vacía"
                        password.length < 4 -> error = "La contraseña debe tener al menos 4 caracteres"
                        else -> {
                            error = null
                            isLoading = true
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Aceptar")
            }

            // Indicador de carga
            if (isLoading) {
                Spacer(modifier = Modifier.height(20.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Verificando credenciales...")
                }
            }

            // Mensaje de error
            error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // Efecto de validación del login (simulación con delay)
    if (isLoading) {
        LaunchedEffect(Unit) {
            delay(2000) // simulacion de 2 segundos

            val result = userViewModel.login(username, password)
            isLoading = false

            if (result == "admin" || result == "user") {
                error = null
                onLoginSuccess(result)
            } else {
                error = "Usuario o contraseña incorrectos"
            }
        }
    }
}
