package com.example.tadpole_g4.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tadpole_g4.data.entity.UsuarioEntity
import com.example.tadpole_g4.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * ViewModel conectado a Room usando UserRepository.
 */
class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    // --------------------------------------------
    // CAMPOS DEL LOGIN
    // --------------------------------------------
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

    fun clearLoginFields() {
        username.value = ""
        password.value = ""
    }

    // --------------------------------------------
    // LISTA DE USUARIOS (Room)
    // --------------------------------------------
    var users = mutableStateListOf<UsuarioEntity>()
        private set

    var currentUser: UsuarioEntity? = null

    init {
        loadUsers()
    }

    /**
     * Carga usuarios desde la base de datos Room.
     */
    private fun loadUsers() {
        viewModelScope.launch {
            users.clear()
            users.addAll(repository.getAllUsers())
        }
    }

    /**
     * Agrega un usuario a Room.
     */
    fun addUser(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.addUser(usuario)
            loadUsers()
        }
    }

    /**
     * Actualiza un usuario.
     */
    fun updateUser(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.updateUser(usuario)
            loadUsers()
        }
    }

    /**
     * Elimina un usuario.
     */
    fun deleteUser(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.deleteUser(usuario)
            loadUsers()
        }
    }

    /**
     * Login usando RUT + contraseña.
     */
    fun login(rutStr: String, pass: String): String? {
        val rut = rutStr.toLongOrNull() ?: return null

        val found = users.find {
            it.rut_usu == rut && it.contrasenia_usu == pass
        }

        currentUser = found

        return when {
            found == null -> null
            found.id_perfil == 1L -> "admin"
            else -> "user"
        }
    }

}
