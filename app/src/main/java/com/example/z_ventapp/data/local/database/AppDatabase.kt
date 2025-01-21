package com.example.z_ventapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.z_ventapp.data.local.dao.ClienteDao
import com.example.z_ventapp.data.local.dao.ProductoDao
import com.example.z_ventapp.data.local.dao.TicketDao
import com.example.z_ventapp.data.local.dao.UsuarioDao
import com.example.z_ventapp.data.local.entity.ClienteEntity
import com.example.z_ventapp.data.local.entity.DetalleTicketEntity
import com.example.z_ventapp.data.local.entity.ProductoEntity
import com.example.z_ventapp.data.local.entity.TicketEntity
import com.example.z_ventapp.data.local.entity.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        ClienteEntity::class,
        ProductoEntity::class,
        TicketEntity::class,
        DetalleTicketEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun clienteDao(): ClienteDao

    abstract fun productoDao(): ProductoDao

    abstract fun ticketDao(): TicketDao

}