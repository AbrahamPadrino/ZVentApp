package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.dao.ProductoDao
import com.example.z_ventapp.data.mapper.ProductoMapper
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val dao: ProductoDao
): ProductoRepository {
    override suspend fun grabar(entidad: Producto): Int {
        return if(entidad.id == 0) {
            if(dao.existeCodigoBarra(entidad.codigobarra) > 0)
                throw Exception("Ya existe un producto con el código de barras ingresado")
            else
                dao.insertar(ProductoMapper.toDatabase(entidad)).toInt()
        } else
            dao.actualizar(ProductoMapper.toDatabase(entidad))
    }

    override suspend fun eliminar(entidad: Producto): Int {
        return dao.eliminar(ProductoMapper.toDatabase(entidad))
    }

    override suspend fun obtenerPorId(id: Int): Producto? {
        return dao.obtenerProductoPorId(id)?.let {
            ProductoMapper.toDomain(it)
        }
    }

    override suspend fun obtenerPorCodigoBarra(codigoBarra: String): Producto? {
        return dao.obtenerProductoPorCodigoBarra(codigoBarra)?.let {
            ProductoMapper.toDomain(it)
        }
    }

    override fun listar(dato: String): Flow<List<Producto>> {
        return dao.listar(dato).map {
            it.map { productoEntity ->
                ProductoMapper.toDomain(productoEntity)
            }
        }
    }
}