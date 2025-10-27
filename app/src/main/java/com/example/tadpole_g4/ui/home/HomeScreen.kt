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
import com.example.tadpole_g4.viewmodel.UserViewModel
import com.example.tadpole_g4.R
import com.example.tadpole_g4.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userViewModel: UserViewModel) {

    // Todas las variables deben ir dentro del Composable
    val users = userViewModel.users
    val currentUser = userViewModel.currentUser

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var editingUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hola, ${currentUser?.username ?: "Usuario"}")
                }
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
            // LOGO HOME
            Image(
                painter = painterResource(id = R.drawable.logo_home),
                contentDescription = "Logo Home",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            // Formulario CRUD
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
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
                    username = ""
                    email = ""
                    password = ""
                }) {
                    Text(if (editingUser != null) "Actualizar" else "Agregar")
                }

                if (editingUser != null) {
                    Button(onClick = {
                        editingUser = null
                        username = ""
                        email = ""
                        password = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de usuarios
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
                                TextButton(onClick = {
                                    editingUser = user
                                    username = user.username
                                    email = user.email
                                    password = user.password
                                }) {
                                    Text("Editar")
                                }
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
