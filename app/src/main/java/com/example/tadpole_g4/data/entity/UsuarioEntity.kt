package com.example.tadpole_g4.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    @ColumnInfo(name = "rut_usu")
    val rut_usu: Long,

    @ColumnInfo(name = "dv_usu")
    val dv_usu: String,

    @ColumnInfo(name = "pnom_usu")
    val pnom_usu: String,

    @ColumnInfo(name = "snom_usu")
    val snom_usu: String,

    @ColumnInfo(name = "appaterno_usu")
    val appaterno_usu: String,

    @ColumnInfo(name = "apmaterno_usu")
    val apmaterno_usu: String,

    @ColumnInfo(name = "email_usu")
    val email_usu: String,

    @ColumnInfo(name = "nombreusuario_usu")
    val nombreusuario_usu: String,

    @ColumnInfo(name = "contrasenia_usu")
    val contrasenia_usu: String,

    @ColumnInfo(name = "id_perfil")
    val id_perfil: Long
)
