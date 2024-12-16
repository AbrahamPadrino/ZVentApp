package com.example.z_ventapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.z_ventapp.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/*Su función es la de interactuar con la base de datos a través de la implementación de funciones*/
@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: UsuarioEntity): Long

    @Update
    suspend fun actualizar(usuario: UsuarioEntity): Int

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity): Int

    @Query("SELECT * FROM usuario Where nombre Like '%' || :nombre || '%'")
    fun listarPorNombre(nombre: String): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuario Where vigente=1 AND email=:email AND clave=:clave")
    suspend fun obtenerUsuario(email: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuario Where id=:id")
    suspend fun obtenerUsuarioPorId(id: Int): UsuarioEntity?

    @Query("SELECT ifnull(count(id), 0) FROM usuario")
    suspend fun existeCuenta(): Int

    @Query("UPDATE usuario SET clave=:clave Where id=:id")
    suspend fun actualizarClave(id: Int, clave: String): Int

    @Transaction
    suspend fun grabarCuenta(usuario: UsuarioEntity): UsuarioEntity?{
        return obtenerUsuarioPorId(
            insertar(usuario).toInt()
        )
    }

}