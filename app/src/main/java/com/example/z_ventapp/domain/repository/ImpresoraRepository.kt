package com.example.z_ventapp.domain.repository

import com.example.z_ventapp.domain.model.Impresora
import kotlinx.coroutines.flow.Flow

interface ImpresoraRepository {

    suspend fun grabar(model: Impresora)

    fun obtener(): Flow<Impresora?>

}