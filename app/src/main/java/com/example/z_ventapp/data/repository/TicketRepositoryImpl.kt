package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.local.dao.TicketDao
import com.example.z_ventapp.data.mapper.DetalleTicketMapper
import com.example.z_ventapp.data.mapper.ReporteCajaMapper
import com.example.z_ventapp.data.mapper.ReporteDetalleTicketMapper
import com.example.z_ventapp.data.mapper.ReporteTicketMapper
import com.example.z_ventapp.data.mapper.ReporteTicketMensualMapper
import com.example.z_ventapp.data.mapper.TicketMapper
import com.example.z_ventapp.domain.model.ReporteCaja
import com.example.z_ventapp.domain.model.ReporteDetalleTicket
import com.example.z_ventapp.domain.model.ReporteTicket
import com.example.z_ventapp.domain.model.ReporteTicketMensual
import com.example.z_ventapp.domain.model.Ticket
import com.example.z_ventapp.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TicketRepositoryImpl @Inject constructor(
    private val ticketDao: TicketDao
): TicketRepository {
    override suspend fun insertar(entidad: Ticket): Int {
        return ticketDao.insertarTicket(
            TicketMapper.toDatabase(entidad),
            entidad.detalles.map { DetalleTicketMapper.toDatabase(it) }
        )
    }

    override suspend fun anularTicket(id: Int): Int {
        return ticketDao.anularTicket(id)
    }

    override fun reporteCajaPorFecha(desde: String, hasta: String): Flow<List<ReporteCaja>> {
      return ticketDao.reporteCajaPorFecha(desde, hasta).map {
          it.map { reporteCaja ->
              ReporteCajaMapper.toDomain(reporteCaja)
          }
      }
    }

    override fun reporteTicketPorFecha(desde: String, hasta: String): Flow<List<ReporteTicket>> {
        return ticketDao.reporteTicketPorFecha(desde, hasta).map {
            it.map { reporteTicket ->
                ReporteTicketMapper.toDomain(reporteTicket)
            }
        }
    }

    override suspend fun reporteDetalleTicket(id: Int): List<ReporteDetalleTicket> {
        return ticketDao.reporteDetalleTicket(id).map {
            ReporteDetalleTicketMapper.toDomain(it)
        }
    }

    override suspend fun obtenerTicketPorId(idticket: Int): ReporteTicket? {
        return ticketDao.obtenerTicketPorId(idticket)?.let {
            ReporteTicketMapper.toDomain(it)
        }
    }

    override fun reporteTicketMensual(anio: String): Flow<List<ReporteTicketMensual>> {
        return ticketDao.reporteTicketMensual(anio).map {
            it.map { reporteTicketMensual ->
                ReporteTicketMensualMapper.toDomain(reporteTicketMensual)
            }
        }
    }
}