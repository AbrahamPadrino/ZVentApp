package com.example.z_ventapp.domain.usecase.producto

import com.example.z_ventapp.domain.repository.ProductoRepository
import javax.inject.Inject

class ListarProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(dato: String) = repository.listar(dato)
}