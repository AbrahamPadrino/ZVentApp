package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.entity.ClienteEntity
import com.example.z_ventapp.data.entity.DetalleTicketEntity
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.model.DetalleTicket

object DetalleTicketMapper {

    // Entity -> Domain
    fun toDomain(entity: DetalleTicketEntity): DetalleTicket{
        return DetalleTicket(
            id = entity.id,
            cantidad = entity.cantidad,
            precio =  entity.precio,
            importe = entity.importe,
            idticket = entity.idticket,
            idproducto = entity.idproducto
        )
    }

    // Domain -> Entity
    fun toDatabase(model: DetalleTicket): DetalleTicketEntity{
        return DetalleTicketEntity(
            id = model.id,
            cantidad = model.cantidad,
            precio =  model.precio,
            importe = model.importe,
            idticket = model.idticket,
            idproducto = model.idproducto
        )
    }
}