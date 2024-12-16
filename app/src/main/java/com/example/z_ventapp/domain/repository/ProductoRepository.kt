package com.example.z_ventapp.domain.repository

import com.example.z_ventapp.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {

    suspend fun grabar(entidad: Producto): Int

    suspend fun eliminar(entidad: Producto): Int

    suspend fun obtenerPorId(id: Int): Producto?

    suspend fun obtenerPorCodigoBarra(codigoBarra: String): Producto?

    fun listar(dato: String): Flow<List<Producto>>

}