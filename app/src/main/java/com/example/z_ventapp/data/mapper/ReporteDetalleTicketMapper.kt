package com.example.z_ventapp.data.mapper
import com.example.z_ventapp.data.local.entity.report.ReporteDetalleTicketEntity
import com.example.z_ventapp.domain.model.ReporteDetalleTicket

object ReporteDetalleTicketMapper {

    fun toDomain(entity: ReporteDetalleTicketEntity): ReporteDetalleTicket {
        return ReporteDetalleTicket(
            descripcion = entity.descripcion,
            precio = entity.precio,
            cantidad = entity.cantidad,
            importe = entity.importe
        )
    }

    fun toDatabase(model: ReporteDetalleTicket): ReporteDetalleTicketEntity {
        return ReporteDetalleTicketEntity(
            descripcion = model.descripcion,
            precio = model.precio,
            cantidad = model.cantidad,
            importe = model.importe
        )
    }

}