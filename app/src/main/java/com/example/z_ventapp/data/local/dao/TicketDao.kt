package com.example.z_ventapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.z_ventapp.data.local.entity.DetalleTicketEntity
import com.example.z_ventapp.data.local.entity.TicketEntity
import com.example.z_ventapp.data.local.entity.report.ReporteCajaEntity
import com.example.z_ventapp.data.local.entity.report.ReporteDetalleTicketEntity
import com.example.z_ventapp.data.local.entity.report.ReporteTicketEntity
import com.example.z_ventapp.data.local.entity.report.ReporteTicketMensualEntity
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

    @Query("SELECT ticket.id, fecha, total, estado, cliente.nombre as cliente " +
            "FROM cliente inner join ticket on cliente.id = ticket.idcliente " +
            "WHERE date(fecha) >= :desde AND date(fecha) <= :hasta ORDER BY ticket.id DESC")
    fun reporteTicketPorFecha(desde: String, hasta: String): Flow<List<ReporteTicketEntity>>

    @Query("SELECT ticket.id, fecha, total, estado, cliente.nombre as cliente " +
            "FROM cliente inner join ticket on cliente.id = ticket.idcliente " +
            "WHERE ticket.id = :idticket")
    suspend fun obtenerTicketPorId(idticket: Int): ReporteTicketEntity?

    @Query("SELECT producto.descripcion, detalleticket.precio, cantidad, importe " +
            "FROM producto inner join detalleticket on producto.id = detalleticket.idproducto " +
            "WHERE idticket=:idticket")
    suspend fun reporteDetalleTicket(idticket: Int): List<ReporteDetalleTicketEntity>

    @Query("SELECT CASE strftime('%m', fecha) " +
            "WHEN '01' THEN 'Ene' " +
            "WHEN '02' THEN 'Feb' " +
            "WHEN '03' THEN 'Mar' " +
            "WHEN '04' THEN 'Abr' " +
            "WHEN '05' THEN 'May' " +
            "WHEN '06' THEN 'Jun' " +
            "WHEN '07' THEN 'Jul' " +
            "WHEN '08' THEN 'Ago' " +
            "WHEN '09' THEN 'Set' " +
            "WHEN '10' THEN 'Oct' " +
            "WHEN '11' THEN 'Nov' " +
            "WHEN '12' THEN 'Dic' " +
            "END AS mes, " +
            "SUM(total) as total, " +
            "strftime('%m', fecha) AS mes_numero " +
            "FROM ticket " +
            "WHERE strftime('%Y', fecha) = :anio AND lower(estado) = lower('vigente') " +
            "GROUP BY mes_numero " +
            "ORDER BY mes_numero ASC"
    )
    fun reporteTicketMensual(anio: String): Flow<List<ReporteTicketMensualEntity>>
}