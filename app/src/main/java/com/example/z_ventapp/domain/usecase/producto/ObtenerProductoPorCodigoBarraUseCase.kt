package com.example.z_ventapp.domain.usecase.producto

import com.example.z_ventapp.domain.repository.ProductoRepository
import javax.inject.Inject

class ObtenerProductoPorCodigoBarraUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(codigoBarra: String) = repository.obtenerPorCodigoBarra(codigoBarra)
}