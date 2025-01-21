package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.storage.EmpresaEntity
import com.example.z_ventapp.domain.model.Empresa

object EmpresaMapper {

    fun toDomain(entity: EmpresaEntity): Empresa{
        return Empresa(
            ruc = entity.ruc,
            razonSocial = entity.razonSocial,
            direccion = entity.direccion,
            telefono = entity.telefono
        )
    }

    fun toDatabase(model: Empresa): EmpresaEntity {
        return EmpresaEntity(
            ruc = model.ruc,
            razonSocial = model.razonSocial,
            direccion = model.direccion,
            telefono = model.telefono
        )
    }

}