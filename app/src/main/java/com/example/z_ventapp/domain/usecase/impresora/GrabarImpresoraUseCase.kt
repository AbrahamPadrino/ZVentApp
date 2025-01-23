package com.example.z_ventapp.domain.usecase.impresora

import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.domain.repository.ImpresoraRepository
import javax.inject.Inject

class GrabarImpresoraUseCase @Inject constructor(
    private val impresoraRepository: ImpresoraRepository
) {

    suspend operator fun invoke(impresora: Impresora) = impresoraRepository.grabar(impresora)

}