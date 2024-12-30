package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.dao.TicketDao
import com.example.z_ventapp.data.mapper.DetalleTicketMapper
import com.example.z_ventapp.data.mapper.TicketMapper
import com.example.z_ventapp.domain.model.Ticket
import com.example.z_ventapp.domain.repository.TicketRepository
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
}