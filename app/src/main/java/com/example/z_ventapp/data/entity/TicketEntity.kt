package com.example.z_ventapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ticket",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["idusuario"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id"],
            childColumns = ["idcliente"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    ignoredColumns = ["detalles"]
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "fecha") var fecha: String = "",
    @ColumnInfo(name = "total") var total: Double = 0.0,
    @ColumnInfo(name = "estado") var estado: String = "",
    @ColumnInfo(name = "idusuario") var idusuario: Int = 0,
    @ColumnInfo(name = "idcliente") var idcliente: Int = 0
){
    var detalles: List<DetalleTicketEntity> = emptyList()
}
