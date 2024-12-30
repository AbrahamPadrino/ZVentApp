package com.example.z_ventapp.domain.repository


import com.example.z_ventapp.domain.model.Ticket

// Intermediar entre la capa de datos y la capa de dominio
interface TicketRepository {
    suspend fun insertar(entidad: Ticket): Int

    suspend fun anularTicket(id: Int): Int

}