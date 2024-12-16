package com.example.z_ventapp.domain.repository

import com.example.z_ventapp.domain.model.Cliente
import kotlinx.coroutines.flow.Flow
// Intermediar entre la capa de datos y la capa de dominio
interface ClienteRepository {

    suspend fun grabar(entidad: Cliente): Int

    suspend fun eliminar(entidad: Cliente): Int

    suspend fun obtenerClientePorId(id: Int): Cliente?

    fun listar(dato: String): Flow<List<Cliente>>
}