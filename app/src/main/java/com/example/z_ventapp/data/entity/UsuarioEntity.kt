package com.example.z_ventapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    indices = [
        Index(value = ["email"], unique = true)
    ]
)

data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "nombre") var nombre: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "clave") var clave: String = "",
    @ColumnInfo(name = "vigente") var vigente: Int = 0
)
