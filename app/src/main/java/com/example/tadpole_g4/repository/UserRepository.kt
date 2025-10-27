package com.example.tadpole_g4.repository

import com.example.tadpole_g4.model.User

class UserRepository {
    private val users = mutableListOf<User>()

    init {
        users.add(User(id = 1, username = "admin", email = "admin@demo.com", password = "1234"))
    }

    fun getAllUsers(): List<User> = users

    fun addUser(user: User) {
        val nextId = (users.maxOfOrNull { it.id } ?: 0) + 1
        users.add(user.copy(id = nextId))
    }

    fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser
        }
    }

    fun deleteUser(user: User) {
        users.removeIf { it.id == user.id }
    }
}
