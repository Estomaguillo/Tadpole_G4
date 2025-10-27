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
    //  Variables del ViewModel: usuarios registrados y usuario logeado
    // -------------------------------------------------------------------
    val users = userViewModel.users
    val currentUser = userViewModel.currentUser

    // -------------------------------------------------------------------
    //  Campos del formulario CRUD (persisten entre pestañas)
    // -------------------------------------------------------------------
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var editingUser by rememberSaveable { mutableStateOf<User?>(null) }

    // Mensaje de error en validaciones
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Control de pestaña activa en menú inferior
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    // Usuario seleccionado temporalmente para eliminación
    var userToDelete by remember { mutableStateOf<User?>(null) }

    val scrollState = rememberScrollState()

    // -------------------------------------------------------------------
    // ESTRUCTURA PRINCIPAL: AppBar superior + menú inferior
    // -------------------------------------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hola, ${currentUser?.username ?: "Usuario"}") },
                actions = {
                    // Botón para cerrar sesión
                    TextButton(onClick = {
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
        // Menú inferior de navegación
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

        // -------------------------------------------------------------------
        // CONTENIDO SEGÚN PESTAÑA ACTIVA
        // -------------------------------------------------------------------
        when (selectedTab) {

            // ============================================================
            // PESTAÑA 1: LISTADO DE USUARIOS (CRUD Visual)
            // ============================================================
            0 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    // Logo para Home UI
                    Image(
                        painter = painterResource(id = R.drawable.logo_home),
                        contentDescription = "Logo Home",
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 24.dp)
                    )

                    Text("Usuarios registrados", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    // Recorre y pinta la lista de usuarios
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
                                        // Cargar datos del usuario para editar
                                        TextButton(onClick = {
                                            selectedTab = 1
                                            editingUser = user
                                            username = user.username
                                            email = user.email
                                            password = user.password
                                        }) {
                                            Text("Editar")
                                        }

                                        // ADMIN NO SE PUEDE ELIMINAR
                                        if (user.username != "admin") {
                                            TextButton(onClick = {
                                                userToDelete = user
                                            }) {
                                                Text("Eliminar")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (users.isEmpty()) {
                        Spacer(Modifier.height(24.dp))
                        Text("No hay usuarios registrados aún.")
                    }
                }
            }

            // ============================================================
            //  PESTAÑA 2: CRUD (Agregar o Editar)
            // ============================================================
            1 -> {

                // Detecta si el usuario que estamos editando es admin
                val isAdmin = editingUser?.username == "admin"

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Título dinámico: Agregar o Editar
                    Text(
                        if (editingUser != null) "Editar usuario" else "Agregar usuario",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(16.dp))

                    // Admin NO puede cambiar su username
                    OutlinedTextField(
                        value = username,
                        onValueChange = { if (!isAdmin) username = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isAdmin
                    )

                    Spacer(Modifier.height(12.dp))

                    // Admin NO puede cambiar su email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { if (!isAdmin) email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isAdmin
                    )

                    Spacer(Modifier.height(12.dp))

                    // El único dato editable SIEMPRE: contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    // Botones CRUD
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            when {
                                password.isBlank() ->
                                    errorMessage = "La contraseña no puede estar vacía"

                                password.length < 4 ->
                                    errorMessage = "Debe tener al menos 4 caracteres"

                                else -> {
                                    // Si es edición → actualiza
                                    if (editingUser != null) {
                                        userViewModel.updateUser(
                                            editingUser!!.copy(
                                                username = username,
                                                email = email,
                                                password = password
                                            )
                                        )
                                        editingUser = null
                                    }
                                    // Si es usuario nuevo → agregar normal
                                    else {
                                        userViewModel.addUser(username, email, password)
                                    }

                                    // Limpia formulario tras guardar
                                    username = ""
                                    email = ""
                                    password = ""
                                    errorMessage = null
                                    selectedTab = 0
                                }
                            }
                        }) {
                            Text(if (editingUser != null) "Actualizar" else "Guardar")
                        }

                        // Botón cancelar solo cuando se edita
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

                    // Mensaje de error CRUD
                    errorMessage?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // ============================================================
            // PESTAÑA 3: CONFIGURACIÓN (en desarrollo)
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
                    Spacer(Modifier.height(16.dp))
                    Text("Aquí puedes agregar opciones del perfil, preferencias o temas.")
                }
            }
        }

        // -------------------------------------------------------------------
        // DIÁLOGO DE CONFIRMACIÓN PARA ELIMINAR
        // EXTRA: Prevención asegurada desde UI (admin nunca llega aquí)
        // -------------------------------------------------------------------
        userToDelete?.let { user ->
            AlertDialog(
                onDismissRequest = { userToDelete = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Seguro que deseas eliminar a \"${user.username}\"?") },
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
