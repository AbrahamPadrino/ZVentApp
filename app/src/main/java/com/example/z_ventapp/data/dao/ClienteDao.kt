package com.example.z_ventapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.z_ventapp.data.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert
    suspend fun insertar(entidad: ClienteEntity): Long

    @Update
    suspend fun actualizar(entidad: ClienteEntity): Int

    @Delete
    suspend fun eliminar(entidad: ClienteEntity): Int

    @Query("SELECT id, nombre, nrodoc, vigente FROM cliente WHERE id = :id")
    suspend fun obtenerClientePorId(id: Int): ClienteEntity?

    @Query("SELECT id, nombre, nrodoc, vigente FROM cliente WHERE nombre Like '%'||:dato||'%'")
    fun listarPorNombre(dato: String): Flow<List<ClienteEntity>>

}