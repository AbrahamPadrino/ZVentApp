package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.local.dao.TicketDao
import com.example.z_ventapp.data.mapper.DetalleTicketMapper
import com.example.z_ventapp.data.mapper.ReporteCajaMapper
import com.example.z_ventapp.data.mapper.TicketMapper
import com.example.z_ventapp.domain.model.ReporteCaja
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
}