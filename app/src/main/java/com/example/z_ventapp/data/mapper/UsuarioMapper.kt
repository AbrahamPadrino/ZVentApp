package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.entity.UsuarioEntity
import com.example.z_ventapp.domain.model.Usuario

object UsuarioMapper {

    // Entity -> Domain
    fun toDomain(entity: UsuarioEntity): Usuario{
        return Usuario(
            id = entity.id,
            nombre = entity.nombre,
            email = entity.email,
            clave = entity.clave,
            vigente = entity.vigente
        )
    }

    // Domain -> Entity
    fun toDatabase(model: Usuario): UsuarioEntity{
        return UsuarioEntity(
            id = model.id,
            nombre = model.nombre,
            email = model.email,
            clave = model.clave,
            vigente = model.vigente
        )
    }
}