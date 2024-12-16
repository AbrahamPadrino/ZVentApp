package com.example.z_ventapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalleticket",
    foreignKeys = [
        ForeignKey(
            entity = TicketEntity::class,
            parentColumns = ["id"],
            childColumns = ["idticket"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idproducto"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class DetalleTicketEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "precio") var precio: Double = 0.0,
    @ColumnInfo(name = "importe") var importe: Double = 0.0,
    @ColumnInfo(name = "cantidad") var cantidad: Int = 0,
    @ColumnInfo(name = "idticket") var idticket: Int = 0,
    @ColumnInfo(name = "idproducto") var idproducto: Int = 0
)