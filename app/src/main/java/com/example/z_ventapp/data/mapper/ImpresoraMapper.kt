package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.storage.ImpresoraEntity
import com.example.z_ventapp.domain.model.Impresora

object ImpresoraMapper {

    fun toDomain(entity: ImpresoraEntity): Impresora {
        return Impresora(
            alias = entity.alias,
            tipo = entity.tipo
        )
    }

    fun toDatabase(model: Impresora): ImpresoraEntity {
        return ImpresoraEntity(
            tipo = model.tipo,
            alias = model.alias
        )
    }

}