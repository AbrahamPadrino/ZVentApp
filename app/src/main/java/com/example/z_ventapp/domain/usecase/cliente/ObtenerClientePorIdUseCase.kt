package com.example.z_ventapp.domain.usecase.cliente

import com.example.z_ventapp.domain.repository.ClienteRepository
import javax.inject.Inject

class ObtenerClientePorIdUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(id: Int) = clienteRepository.obtenerClientePorId(id)
}