package com.example.z_ventapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.z_ventapp.data.dao.ClienteDao
import com.example.z_ventapp.data.dao.ProductoDao
import com.example.z_ventapp.data.dao.UsuarioDao
import com.example.z_ventapp.data.entity.ClienteEntity
import com.example.z_ventapp.data.entity.DetalleTicketEntity
import com.example.z_ventapp.data.entity.ProductoEntity
import com.example.z_ventapp.data.entity.TicketEntity
import com.example.z_ventapp.data.entity.UsuarioEntity

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

}