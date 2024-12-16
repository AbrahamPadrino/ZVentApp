package com.example.z_ventapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "producto",
    indices = [
        Index(value = ["codigobarra"], unique = true)
    ]
)

data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "descripcion") var descripcion: String = "",
    @ColumnInfo(name = "codigobarra") var codigobarra: String = "",
    @ColumnInfo(name = "precio") var precio: Double = 0.0
)
