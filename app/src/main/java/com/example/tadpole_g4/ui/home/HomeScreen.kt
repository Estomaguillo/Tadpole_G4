package com.example.tadpole_g4.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
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
    //  Variables del ViewModel
    // -------------------------------------------------------------------
    val users = userViewModel.users
    val currentUser = userViewModel.currentUser

    // -------------------------------------------------------------------
    //  Campos del formulario CRUD (persistentes)
    // -------------------------------------------------------------------
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var editingUser by rememberSaveable { mutableStateOf<User?>(null) }

    //  Mensaje de error
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    //  Estado de pestaña seleccionada en el menú inferior
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    //  Usuario seleccionado para eliminar (null = sin selección)
    var userToDelete by remember { mutableStateOf<User?>(null) }

    val scrollState = rememberScrollState()

    // -------------------------------------------------------------------
    //  Estructura principal con barra superior + menú inferior
    // -------------------------------------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hola, ${currentUser?.username ?: "Usuario"}") },
                actions = {
                    TextButton(onClick = {
                        // Limpia sesión y navega al login
                        userViewModel.clearLoginFields()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
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

        // -------------------------------------------------------------------
        //  MENÚ INFERIOR DE NAVEGACIÓN ENTRE SECCIONES
        // -------------------------------------------------------------------
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Usuarios") },
                    label = { Text("Usuarios") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Agregar") },
                    label = { Text("Agregar") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
                    label = { Text("Configuración") }
                )
            }
        }
    ) { padding ->

        // ================================================================
        // CONTENIDO VARIABLE SEGÚN LA PESTAÑA SELECCIONADA
        // ================================================================
        when (selectedTab) {

            // ============================================================
            // PESTAÑA 1: LISTA DE USUARIOS
            // ============================================================
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_home),
                        contentDescription = "Logo Home",
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 24.dp)
                    )

                    Text(
                        "Usuarios registrados",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de usuarios
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
                                        // ----------------------------------------------------------
                                        // BOTÓN EDITAR: cambia a pestaña "Agregar" con datos cargados
                                        // ----------------------------------------------------------
                                        TextButton(onClick = {
                                            selectedTab = 1
                                            editingUser = user
                                            username = user.username
                                            email = user.email
                                            password = user.password
                                        }) {
                                            Text("Editar")
                                        }

                                        // ----------------------------------------------------------
                                        // BOTÓN ELIMINAR: muestra diálogo de confirmación
                                        // ----------------------------------------------------------
                                        TextButton(onClick = {
                                            userToDelete = user // Guarda el usuario a eliminar
                                        }) {
                                            Text("Eliminar")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (users.isEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("No hay usuarios registrados aún.")
                    }
                }
            }

            // ============================================================
            // PESTAÑA 2: FORMULARIO CRUD (Agregar / Editar)
            // ============================================================
            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        if (editingUser != null) "Editar usuario" else "Agregar usuario",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; errorMessage = null },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ----------------------------------------------------
                    // BOTONES DE ACCIÓN (Guardar / Actualizar / Cancelar)
                    // ----------------------------------------------------
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            when {
                                username.isBlank() -> errorMessage = "El nombre de usuario no puede estar vacío"
                                email.isBlank() -> errorMessage = "El email no puede estar vacío"
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                                    errorMessage = "El formato del email no es válido"
                                password.isBlank() -> errorMessage = "La contraseña no puede estar vacía"
                                password.length < 4 -> errorMessage = "Debe tener al menos 4 caracteres"
                                else -> {
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
                                    errorMessage = null
                                    selectedTab = 0 // Volver a la lista después de guardar
                                }
                            }
                        }) {
                            Text(if (editingUser != null) "Actualizar" else "Guardar")
                        }

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

                    // ----------------------------------------------------
                    // MENSAJE DE ERROR
                    // ----------------------------------------------------
                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // ============================================================
            // PESTAÑA 3: CONFIGURACIÓN / PERFIL
            // ============================================================
            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Configuración", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Aquí puedes agregar opciones del perfil, preferencias o temas.")
                }
            }
        }

        // ================================================================
        // DIÁLOGO DE CONFIRMACIÓN PARA ELIMINAR USUARIO
        // ================================================================
        userToDelete?.let { user ->
            AlertDialog(
                onDismissRequest = { userToDelete = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que deseas eliminar al usuario \"${user.username}\"?") },
                confirmButton = {
                    TextButton(onClick = {
                        userViewModel.deleteUser(user)
                        userToDelete = null
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { userToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
