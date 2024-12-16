package com.example.z_ventapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cliente",
    indices = [
        Index(value = ["nrodoc"], unique = true)
    ]
)

data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "nombre") var nombre: String = "",
    @ColumnInfo(name = "nrodoc") var nrodoc: String = "",
    @ColumnInfo(name = "vigente") var vigente: Int = 0
)
