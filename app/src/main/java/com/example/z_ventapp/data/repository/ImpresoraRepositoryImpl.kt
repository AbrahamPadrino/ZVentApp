package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.mapper.ImpresoraMapper
import com.example.z_ventapp.data.storage.LocalDataStore
import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.domain.repository.ImpresoraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImpresoraRepositoryImpl @Inject constructor(
    private val localDataSotre: LocalDataStore
) : ImpresoraRepository {

    override suspend fun grabar(model: Impresora) {
        localDataSotre.grabarImpresora(
            ImpresoraMapper.toDatabase(model)
        )
    }

    override fun obtener(): Flow<Impresora?> {
        return localDataSotre.obtenerImpresora().map {
            it?.let { entity ->
                ImpresoraMapper.toDomain(entity)
            }
        }
    }
}