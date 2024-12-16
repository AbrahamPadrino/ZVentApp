package com.example.z_ventapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.z_ventapp.data.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertar(producto: ProductoEntity): Long

    @Update
    suspend fun actualizar(producto: ProductoEntity): Int

    @Delete
    suspend fun eliminar(producto: ProductoEntity): Int

    @Query("SELECT count(id) FROM producto Where codigobarra = :codigobarra")
    suspend fun existeCodigoBarra(codigobarra: String): Int

    @Query("SELECT id, descripcion, codigobarra, precio FROM producto Where id = :id")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    @Query("SELECT id, descripcion, codigobarra, precio FROM producto Where codigobarra = :codigobarra")
    suspend fun obtenerProductoPorCodigoBarra(codigobarra: String): ProductoEntity?

    @Query("SELECT id, descripcion, codigobarra, precio FROM producto Where descripcion LIKE '%' || :dato || '%'")
    fun listar(dato: String): Flow<List<ProductoEntity>>
}