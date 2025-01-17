package com.example.z_ventapp.domain.repository

import com.example.z_ventapp.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {

    suspend fun grabar(usuario: Usuario): Int

    suspend fun eliminar(usuario: Usuario): Int

    suspend fun actualizarClave(id: Int, clave: String): Int

    suspend fun obtenerUsuario(email: String, clave: String): Usuario?

    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    suspend fun existeCuenta(): Int

    suspend fun grabarCuenta(usuario: Usuario): Usuario?

    fun listar(dato: String): Flow<List<Usuario>>

}