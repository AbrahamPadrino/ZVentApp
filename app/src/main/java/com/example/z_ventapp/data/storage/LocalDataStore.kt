package com.example.z_ventapp.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("app_preferences")

class LocalDataStore(private val context: Context) {

    companion object {

        private val EMPRESA_RAZONSOCIAL_KEY = stringPreferencesKey("empresa_razonsocial")
        private val EMPRESA_RUC_KEY = stringPreferencesKey("empresa_ruc")
        private val EMPRESA_DIRECCION_KEY = stringPreferencesKey("empresa_direccion")
        private val EMPRESA_TELEFONO_KEY = stringPreferencesKey("empresa_telefono")
    }

    suspend fun grabarEmpresa(model: EmpresaEntity) {
        context.dataStore.edit {
            it[EMPRESA_RAZONSOCIAL_KEY] = model.razonSocial
            it[EMPRESA_RUC_KEY] = model.ruc
            it[EMPRESA_DIRECCION_KEY] = model.direccion
            it[EMPRESA_TELEFONO_KEY] = model.telefono
        }
    }

    fun obtenerEmpresa(): Flow<EmpresaEntity?> = context.dataStore.data.map {
        EmpresaEntity(
            razonSocial = it[EMPRESA_RAZONSOCIAL_KEY] ?: "",
            ruc = it[EMPRESA_RUC_KEY] ?: "",
            direccion = it[EMPRESA_DIRECCION_KEY] ?: "",
            telefono = it[EMPRESA_TELEFONO_KEY] ?: ""
        )
    }
}