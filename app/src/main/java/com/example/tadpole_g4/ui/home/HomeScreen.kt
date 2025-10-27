package com.example.tadpole_g4.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tadpole_g4.viewmodel.UserViewModel
import com.example.tadpole_g4.R
import com.example.tadpole_g4.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userViewModel: UserViewModel, navController: NavController) {

    // -------------------------------------------------------------------
    // 🔹 VARIABLES PRINCIPALES DEL VIEWMODEL
    // -------------------------------------------------------------------
    val users = userViewModel.users
    val currentUser = userViewModel.currentUser

    // -------------------------------------------------------------------
    // 🔹 CAMPOS DEL FORMULARIO (Se mantienen al rotar la pantalla)
    // -------------------------------------------------------------------
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var editingUser by rememberSaveable { mutableStateOf<User?>(null) }

    // 🔹 Mensaje de error (también persistente)
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // 🔹 Scroll para pantallas pequeñas o modo horizontal
    val scrollState = rememberScrollState()

    // -------------------------------------------------------------------
    // 🔹 INTERFAZ PRINCIPAL
    // -------------------------------------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hola, ${currentUser?.username ?: "Usuario"}")
                },
                actions = {
                    // BOTÓN CERRAR SESIÓN
                    TextButton(onClick = {
                        // Limpiar los campos del login
                        userViewModel.clearLoginFields()

                        // Navegar al Login y limpiar la pila de navegación
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Text(
                            "Cerrar sesión",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        // -------------------------------------------------------------------
        // 🔹 CONTENIDO PRINCIPAL CON SCROLL
        // -------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------------------------------------------------------
            // 🔹 LOGO HOME
            // -------------------------------------------------------------------
            Image(
                painter = painterResource(id = R.drawable.logo_home),
                contentDescription = "Logo Home",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )

            // -------------------------------------------------------------------
            // 🔹 FORMULARIO CRUD DE USUARIOS
            // -------------------------------------------------------------------
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null // Limpia error al escribir
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------------------------------------------------------------------
            // 🔹 BOTONES DE ACCIÓN (Agregar / Actualizar / Cancelar)
            // -------------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    // Validaciones antes de agregar o actualizar
                    when {
                        username.isBlank() -> errorMessage = "El nombre de usuario no puede estar vacío"
                        email.isBlank() -> errorMessage = "El email no puede estar vacío"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                            errorMessage = "El formato del email no es válido"
                        password.isBlank() -> errorMessage = "La contraseña no puede estar vacía"
                        password.length < 4 -> errorMessage = "La contraseña debe tener al menos 4 caracteres"
                        else -> {
                            if (editingUser != null) {
                                // Actualizar usuario existente
                                val updatedUser = editingUser!!.copy(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                                userViewModel.updateUser(updatedUser)
                                editingUser = null
                            } else {
                                // Agregar nuevo usuario
                                userViewModel.addUser(username, email, password)
                            }

                            // Limpiar campos después de guardar
                            username = ""
                            email = ""
                            password = ""
                            errorMessage = null
                        }
                    }
                }) {
                    Text(if (editingUser != null) "Actualizar" else "Agregar")
                }

                // Botón cancelar visible solo en modo edición
                if (editingUser != null) {
                    Button(onClick = {
                        editingUser = null
                        username = ""
                        email = ""
                        password = ""
                        errorMessage = null
                    }) {
                        Text("Cancelar")
                    }
                }
            }

            // -------------------------------------------------------------------
            // 🔹 MENSAJE DE ERROR DE VALIDACIÓN
            // -------------------------------------------------------------------
            errorMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -------------------------------------------------------------------
            // 🔹 LISTA DE USUARIOS REGISTRADOS
            // -------------------------------------------------------------------
            Text("Lista de usuarios", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                users.forEach { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(user.username, style = MaterialTheme.typography.bodyLarge)
                                Text(user.email, style = MaterialTheme.typography.bodySmall)
                            }
                            Row {
                                // Botón Editar
                                TextButton(onClick = {
                                    editingUser = user
                                    username = user.username
                                    email = user.email
                                    password = user.password
                                    errorMessage = null
                                }) {
                                    Text("Editar")
                                }

                                // Botón Eliminar
                                TextButton(onClick = {
                                    userViewModel.deleteUser(user)
                                }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
