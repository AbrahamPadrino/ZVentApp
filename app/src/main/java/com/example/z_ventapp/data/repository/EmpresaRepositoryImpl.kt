package com.example.z_ventapp.data.repository

import com.example.z_ventapp.data.mapper.EmpresaMapper
import com.example.z_ventapp.data.storage.LocalDataStore
import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.repository.EmpresaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpresaRepositoryImpl @Inject constructor(
    private val localDataSotre: LocalDataStore
) : EmpresaRepository {

    override suspend fun grabar(model: Empresa) {
        localDataSotre.grabarEmpresa(
            EmpresaMapper.toDatabase(model)
        )
    }

    override fun obtener(): Flow<Empresa?> {
        return localDataSotre.obtenerEmpresa().map {
            it?.let { entity ->
                EmpresaMapper.toDomain(entity)
            }
        }
    }
}