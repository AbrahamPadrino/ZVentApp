package com.example.z_ventapp.data.mapper
import com.example.z_ventapp.data.local.entity.report.ReporteTicketEntity
import com.example.z_ventapp.domain.model.ReporteTicket

object ReporteTicketMapper {

    fun toDomain(entity: ReporteTicketEntity): ReporteTicket {
        return ReporteTicket(
            id = entity.id,
            fecha = entity.fecha,
            total = entity.total,
            estado = entity.estado,
            cliente = entity.cliente
        )
    }

    fun toDatabase(model: ReporteTicket): ReporteTicketEntity {
        return ReporteTicketEntity(
            id = model.id,
            fecha = model.fecha,
            total = model.total,
            estado = model.estado,
            cliente = model.cliente
        )
    }

}