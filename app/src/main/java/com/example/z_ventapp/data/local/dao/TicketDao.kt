package com.example.z_ventapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.z_ventapp.data.local.entity.DetalleTicketEntity
import com.example.z_ventapp.data.local.entity.TicketEntity
import com.example.z_ventapp.data.local.entity.report.ReporteCajaEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT usuario.nombre as usuario, date(fecha) as fecha, sum(total) as total " +
            "FROM usuario inner join ticket on usuario.id = ticket.idusuario " +
            "WHERE date(fecha) BETWEEN :desde AND :hasta AND lower(ticket.estado) = lower('vigente') " +
            "GROUP BY date(fecha), usuario.nombre ORDER BY date(fecha) DESC")
    fun reporteCajaPorFecha(desde: String, hasta: String): Flow<List<ReporteCajaEntity>>
}