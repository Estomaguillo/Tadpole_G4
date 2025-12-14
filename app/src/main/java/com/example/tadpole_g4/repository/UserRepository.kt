package com.example.tadpole_g4.repository

import com.example.tadpole_g4.data.dao.UsuarioDao
import com.example.tadpole_g4.data.entity.UsuarioEntity

class UserRepository(private val usuarioDao: UsuarioDao) {

    suspend fun insertUser(usuario: UsuarioEntity) {
        usuarioDao.insert(usuario)
    }

    suspend fun updateUser(usuario: UsuarioEntity) {
        usuarioDao.update(usuario)
    }

    suspend fun deleteUser(usuario: UsuarioEntity) {
        usuarioDao.delete(usuario)
    }

    suspend fun getUserByRut(rut: Long): UsuarioEntity? {
        return usuarioDao.findByRut(rut)
    }

    suspend fun getAllUsers(): List<UsuarioEntity> {
        return usuarioDao.getAll()
    }
}
