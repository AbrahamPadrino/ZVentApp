package com.example.z_ventapp.domain.repository


import com.example.z_ventapp.domain.model.ReporteCaja
import com.example.z_ventapp.domain.model.ReporteDetalleTicket
import com.example.z_ventapp.domain.model.ReporteTicket
import com.example.z_ventapp.domain.model.ReporteTicketMensual
import com.example.z_ventapp.domain.model.Ticket
import kotlinx.coroutines.flow.Flow

// Intermediar entre la capa de datos y la capa de dominio
interface TicketRepository {
    suspend fun insertar(entidad: Ticket): Int

    suspend fun anularTicket(id: Int): Int

    fun reporteCajaPorFecha(desde: String, hasta: String): Flow<List<ReporteCaja>>

     fun reporteTicketPorFecha(desde: String, hasta: String): Flow<List<ReporteTicket>>

     suspend fun reporteDetalleTicket(id: Int): List<ReporteDetalleTicket>

     suspend fun obtenerTicketPorId(idticket: Int): ReporteTicket?

     fun reporteTicketMensual(anio: String): Flow<List<ReporteTicketMensual>>

}