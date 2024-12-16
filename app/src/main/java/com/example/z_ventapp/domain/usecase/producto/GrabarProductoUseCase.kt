package com.example.z_ventapp.domain.usecase.producto

import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.repository.ProductoRepository
import javax.inject.Inject

class GrabarProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(entidad: Producto) = repository.grabar(entidad)
}