package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.dao.ClienteDao
import com.example.z_ventapp.data.mapper.ClienteMapper
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClienteRepositoryImpl @Inject constructor(
    private val clienteDao: ClienteDao
) : ClienteRepository {

    override suspend fun grabar(entidad: Cliente): Int {
        return if(entidad.id == 0)
            clienteDao.insertar(ClienteMapper.toDatabase(entidad)).toInt()
        else
            clienteDao.actualizar(ClienteMapper.toDatabase(entidad))
    }

    override suspend fun eliminar(entidad: Cliente): Int {
        return clienteDao.eliminar(ClienteMapper.toDatabase(entidad))
    }

    override suspend fun obtenerClientePorId(id: Int): Cliente? {
        return clienteDao.obtenerClientePorId(id)?.let {
            ClienteMapper.toDomain(it)
        }
    }

    override fun listar(dato: String): Flow<List<Cliente>> {
        return clienteDao.listarPorNombre(dato).map {
            it.map { clienteEntity ->
                ClienteMapper.toDomain(clienteEntity)
            }
        }
    }
}