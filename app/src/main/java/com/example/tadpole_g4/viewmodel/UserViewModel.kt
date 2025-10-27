package com.example.tadpole_g4.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tadpole_g4.model.User
import com.example.tadpole_g4.repository.UserRepository

/**
 * ViewModel que maneja la lógica de usuarios y el estado del login.
 * Se conserva en memoria mientras la app esté activa, lo que permite
 * mantener los datos al rotar la pantalla sin perderlos.
 */
class UserViewModel : ViewModel() {

    // -------------------------------------------------------------------
    // CAMPOS DE LOGIN (Persisten al rotar la pantalla)
    // -------------------------------------------------------------------

    // Campo de nombre de usuario
    var username = mutableStateOf("")
        private set

    // Campo de contraseña
    var password = mutableStateOf("")
        private set

    /**
     * Actualiza el valor del campo de usuario
     * Se llama desde el TextField del LoginScreen.
     */
    fun onUsernameChange(newValue: String) {
        username.value = newValue
    }

    /**
     * Actualiza el valor del campo de contraseña
     * Se llama desde el TextField del LoginScreen.
     */
    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    /**
     * Limpia los campos del login.
     * Se usa cuando el usuario cierra sesión para reiniciar el formulario.
     */
    fun clearLoginFields() {
        username.value = ""
        password.value = ""
    }

    // -------------------------------------------------------------------
    // GESTIÓN DE USUARIOS (CRUD y sesión actual)
    // -------------------------------------------------------------------

    private val repository = UserRepository()

    // Lista observable de usuarios que Compose escucha para refrescar la UI
    var users = mutableStateListOf<User>()
        private set

    // Usuario actualmente autenticado
    var currentUser: User? = null

    init {
        // Inicializa la lista de usuarios con los datos del repositorio
        users.addAll(repository.getAllUsers())
        currentUser = users.firstOrNull()
    }

    /**
     * Agrega un nuevo usuario al repositorio y actualiza la lista.
     */
    fun addUser(username: String, email: String, password: String) {
        val newUser = User(username = username, email = email, password = password)
        repository.addUser(newUser)
        refreshUsers()
    }

    /**
     * Actualiza la información de un usuario existente.
     */
    fun updateUser(user: User) {
        repository.updateUser(user)
        refreshUsers()
    }

    /**
     * Elimina un usuario del repositorio y actualiza la lista.
     */
    fun deleteUser(user: User) {
        repository.deleteUser(user)
        refreshUsers()
    }

    /**
     * Verifica las credenciales del usuario.
     * Si son correctas, asigna el usuario actual.
     * @return true si el login es válido, false en caso contrario.
     */
    fun login(username: String, password: String): Boolean {
        val found = users.find { it.username == username && it.password == password }
        return if (found != null) {
            currentUser = found
            true
        } else false
    }

    /**
     * Refresca la lista observable de usuarios.
     * Se usa después de cualquier operación CRUD.
     */
    private fun refreshUsers() {
        users.clear()
        users.addAll(repository.getAllUsers())
    }
}
