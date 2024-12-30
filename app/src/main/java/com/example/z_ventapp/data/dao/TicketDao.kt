package com.example.z_ventapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.z_ventapp.data.entity.DetalleTicketEntity
import com.example.z_ventapp.data.entity.TicketEntity

@Dao
interface TicketDao {

    @Insert
    suspend fun insertar(entidad: TicketEntity): Long

    @Insert
    suspend fun insertarDetalle(entidad: DetalleTicketEntity): Long

    @Query("UPDATE ticket SET estado = 'Anulado' WHERE id = :id")
    suspend fun anularTicket(id: Int): Int

    @Transaction
    suspend fun insertarTicket(entidad: TicketEntity, detalles: List<DetalleTicketEntity>): Int {
        val idTicket = insertar(entidad)
        detalles.forEach {
            it.idticket = idTicket.toInt()
            insertarDetalle(it)
        }
        return idTicket.toInt()
    }
}