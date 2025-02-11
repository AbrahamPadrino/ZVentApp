package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.local.entity.report.ReporteTicketMensualEntity
import com.example.z_ventapp.domain.model.ReporteTicketMensual

object ReporteTicketMensualMapper {

    fun toDomain(entity: ReporteTicketMensualEntity): ReporteTicketMensual {
        return ReporteTicketMensual(
            mes = entity.mes,
            total = entity.total
        )
    }

    fun toDatabase(model: ReporteTicketMensual): ReporteTicketMensualEntity {
        return ReporteTicketMensualEntity(
            mes = model.mes,
            total = model.total
        )
    }

}