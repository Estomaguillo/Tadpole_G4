package com.example.tadpole_g4.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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


    // VARIABLES PRINCIPALES

    val users = userViewModel.users
    val currentUser = userViewModel.currentUser

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var editingUser by remember { mutableStateOf<User?>(null) }

    // variable de error para mostrar mensajes de validación
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hola, ${currentUser?.username ?: "Usuario"}")
                },
                actions = {
                    //Botón para cerrar sesión
                    TextButton(onClick = {
                        // Navegar al Login y limpiar la pila para evitar volver atrás
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //LOGO HOME
            Image(
                painter = painterResource(id = R.drawable.logo_home),
                contentDescription = "Logo Home",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            // FORMULARIO CRUD
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = null // limpiar error al escribir
                },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null //limpiar error al escribir
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null // limpiar error al escribir
                },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            //BOTONES DE ACCIÓN (CON VALIDACIÓN)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    // Validaciones antes de agregar/actualizar
                    when {
                        username.isBlank() -> {
                            errorMessage = "El nombre de usuario no puede estar vacío"
                        }
                        email.isBlank() -> {
                            errorMessage = "El email no puede estar vacío"
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "El formato del email no es válido"
                        }
                        password.isBlank() -> {
                            errorMessage = "La contraseña no puede estar vacía"
                        }
                        password.length < 4 -> {
                            errorMessage = "La contraseña debe tener al menos 4 caracteres"
                        }
                        else -> {
                            //  agregar o actualizar usuario
                            if (editingUser != null) {
                                val updatedUser = editingUser!!.copy(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                                userViewModel.updateUser(updatedUser)
                                editingUser = null
                            } else {
                                userViewModel.addUser(username, email, password)
                            }

                            //limpiar campos al guardar
                            username = ""
                            email = ""
                            password = ""
                            errorMessage = null
                        }
                    }
                }) {
                    Text(if (editingUser != null) "Actualizar" else "Agregar")
                }

                if (editingUser != null) {
                    Button(onClick = {
                        //cancelar edición
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

            //  Mostrar mensaje de error de validación
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            // LISTA DE USUARIOS

            Text("Lista de usuarios", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(users) { user ->
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
                                // Botón editar
                                TextButton(onClick = {
                                    editingUser = user
                                    username = user.username
                                    email = user.email
                                    password = user.password
                                    errorMessage = null
                                }) {
                                    Text("Editar")
                                }
                                //Botón eliminar
                                TextButton(onClick = { userViewModel.deleteUser(user) }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
