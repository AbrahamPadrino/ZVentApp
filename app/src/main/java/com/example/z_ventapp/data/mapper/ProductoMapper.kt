package com.example.z_ventapp.data.mapper

import com.example.z_ventapp.data.entity.ProductoEntity
import com.example.z_ventapp.domain.model.Producto

object ProductoMapper {

    // Entity -> Domain
    fun toDomain(entity: ProductoEntity): Producto{
        return Producto(
            id = entity.id,
            descripcion = entity.descripcion,
            codigobarra = entity.codigobarra,
            precio = entity.precio
        )
    }

    // Domain -> Entity
    fun toDatabase(model: Producto): ProductoEntity{
        return ProductoEntity(
            id = model.id,
            descripcion = model.descripcion,
            codigobarra = model.codigobarra,
            precio = model.precio
        )
    }
}