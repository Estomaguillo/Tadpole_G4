package com.example.tadpole_g4.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tadpole_g4.model.User
import com.example.tadpole_g4.repository.UserRepository

/**
 * ViewModel que maneja la lógica de usuarios y el estado del login.
 * Se conserva en memoria mientras la app esté activa, manteniendo
 * los datos al rotar la pantalla sin perderlos.
 */
class UserViewModel : ViewModel() {

    // -------------------------------------------------------------------
    // CAMPOS DE LOGIN (Persisten al rotar pantalla gracias a Compose)
    // -------------------------------------------------------------------

    var username = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    fun onUsernameChange(newValue: String) {
        username.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    /**
     * Limpia los campos del login.
     * Se usa cuando un usuario cierra sesión.
     */
    fun clearLoginFields() {
        username.value = ""
        password.value = ""
    }

    // -------------------------------------------------------------------
    // GESTIÓN DE USUARIOS (CRUD + manejo de sesión activa)
    // -------------------------------------------------------------------

    private val repository = UserRepository()

    // Lista de usuario
    var users = mutableStateListOf<User>()
        private set

    // Usuario actualmente autenticado
    var currentUser: User? = null

    init {
        // Inicializa usuarios desde el repositorio
        users.addAll(repository.getAllUsers())
        currentUser = null
    }

    /**
     * Agrega un nuevo usuario.
     * El administrador no puede ser creado nuevamente,
     * y el username debe ser único.
     */
    fun addUser(username: String, email: String, password: String): Boolean {
        if (users.any { it.username == username }) return false

        val newUser = User(username = username, email = email, password = password)
        repository.addUser(newUser)
        refreshUsers()
        return true
    }

    /**
     * Actualiza usuario.
     * Restricciones:
     * Admin solo puede actualizar SU contraseña
     * Admin NO puede cambiar username ni email
     */
    fun updateUser(user: User): Boolean {
        if (user.username == "admin") {
            val original = users.find { it.username == "admin" } ?: return false
            val adminUpdated = original.copy(password = user.password)
            repository.updateUser(adminUpdated)
        } else {
            repository.updateUser(user)
        }
        refreshUsers()
        return true
    }

    /**
     * Elimina un usuario.
     * No se permite eliminar al admin.
     */
    fun deleteUser(user: User): Boolean {
        if (user.username == "admin") return false
        repository.deleteUser(user)
        refreshUsers()
        return true
    }

    /**
     * Verifica credenciales y asigna usuario actual si coincide.
     * @return:
     *  * "admin" → pantalla de administrador
     *  * "user"  → pantalla de usuario normal
     *  * null    → login incorrecto
     */
    fun login(username: String, password: String): String? {
        val found = users.find { it.username == username && it.password == password }
        currentUser = found
        return when {
            found == null -> null
            found.username == "admin" -> "admin"
            else -> "user"
        }
    }

    /**
     * Recarga lista de usuarios.
     */
    private fun refreshUsers() {
        users.clear()
        users.addAll(repository.getAllUsers())
    }
}
