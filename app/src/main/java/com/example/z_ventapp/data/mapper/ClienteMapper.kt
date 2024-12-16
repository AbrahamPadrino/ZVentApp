package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.entity.ClienteEntity
import com.example.z_ventapp.domain.model.Cliente

object ClienteMapper {

    // Entity -> Domain
    fun toDomain(entity: ClienteEntity): Cliente{
        return Cliente(
            id = entity.id,
            nombre = entity.nombre,
            nrodoc = entity.nrodoc,
            vigente = entity.vigente
        )
    }

    // Domain -> Entity
    fun toDatabase(model: Cliente): ClienteEntity{
        return ClienteEntity(
            id = model.id,
            nombre = model.nombre,
            nrodoc = model.nrodoc,
            vigente = model.vigente
        )
    }
}