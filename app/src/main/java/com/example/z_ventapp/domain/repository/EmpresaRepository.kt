package com.example.z_ventapp.domain.repository

import com.example.z_ventapp.domain.model.Empresa
import kotlinx.coroutines.flow.Flow

interface EmpresaRepository {

    suspend fun grabar(model: Empresa)

    fun obtener(): Flow<Empresa?>

}