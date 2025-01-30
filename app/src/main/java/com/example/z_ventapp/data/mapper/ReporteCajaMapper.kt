package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.local.entity.report.ReporteCajaEntity
import com.example.z_ventapp.domain.model.ReporteCaja

object ReporteCajaMapper {

    fun toDomain(entity: ReporteCajaEntity): ReporteCaja {
        return ReporteCaja(
            usuario = entity.usuario,
            fecha = entity.fecha,
            total = entity.total
        )
    }

    fun toDatabase(model: ReporteCaja): ReporteCajaEntity {
        return ReporteCajaEntity(
            usuario = model.usuario,
            fecha = model.fecha,
            total = model.total
        )
    }

}