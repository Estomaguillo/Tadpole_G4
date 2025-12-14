package com.example.tadpole_g4.data.dao

import androidx.room.*
import com.example.tadpole_g4.data.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity)

    @Update
    suspend fun update(usuario: UsuarioEntity)

    @Delete
    suspend fun delete(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE rut_usu = :rut LIMIT 1")
    suspend fun findByRut(rut: Long): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<UsuarioEntity>
}