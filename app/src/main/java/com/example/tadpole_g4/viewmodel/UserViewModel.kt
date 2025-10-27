package com.example.tadpole_g4.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.tadpole_g4.model.User
import com.example.tadpole_g4.repository.UserRepository

class UserViewModel : ViewModel() {
    // Campos del login que deben persistir al rotar la pantalla
    var username = androidx.compose.runtime.mutableStateOf("")
        private set

    var password = androidx.compose.runtime.mutableStateOf("")
        private set

    fun onUsernameChange(newValue: String) {
        username.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        password.value = newValue
    }

    private val repository = UserRepository()

    // Estado observable en Compose
    var users = mutableStateListOf<User>()
        private set

    var currentUser: User? = null

    init {
        users.addAll(repository.getAllUsers())
        currentUser = users.firstOrNull()
    }

    fun addUser(username: String, email: String, password: String) {
        val newUser = User(username = username, email = email, password = password)
        repository.addUser(newUser)
        refreshUsers()
    }

    fun updateUser(user: User) {
        repository.updateUser(user)
        refreshUsers()
    }

    fun deleteUser(user: User) {
        repository.deleteUser(user)
        refreshUsers()
    }

    fun login(username: String, password: String): Boolean {
        val found = users.find { it.username == username && it.password == password }
        return if (found != null) {
            currentUser = found
            true
        } else false
    }

    private fun refreshUsers() {
        users.clear()
        users.addAll(repository.getAllUsers())
    }
}
