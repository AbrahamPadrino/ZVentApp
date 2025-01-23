package com.example.z_ventapp.domain.usecase.impresora

import com.example.z_ventapp.domain.repository.ImpresoraRepository
import javax.inject.Inject

class ObtenerImpresoraUseCase @Inject constructor(
    private val impresoraRepository: ImpresoraRepository
) {

    operator fun invoke() = impresoraRepository.obtener()

}