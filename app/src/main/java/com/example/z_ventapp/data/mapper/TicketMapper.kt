package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.entity.ClienteEntity
import com.example.z_ventapp.data.entity.TicketEntity
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.model.Ticket

object TicketMapper {

    // Entity -> Domain
    fun toDomain(entity: TicketEntity): Ticket{
        return Ticket(
            id = entity.id,
            fecha = entity.fecha,
            total = entity.total,
            estado = entity.estado,
            idusuario = entity.idusuario,
            cliente = Cliente().apply { id = entity.idcliente },
            detalles = entity.detalles.map {
                DetalleTicketMapper.toDomain(it)
            }
        )
    }

    // Domain -> Entity
    fun toDatabase(model: Ticket): TicketEntity{
        return TicketEntity(
            id = model.id,
            fecha = model.fecha,
            total = model.total,
            estado = model.estado,
            idusuario = model.idusuario,
            idcliente = model.cliente?.id ?: 0
        )
    }
}