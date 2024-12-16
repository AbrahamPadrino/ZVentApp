package com.example.z_ventapp.domain.usecase.cliente

import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.repository.ClienteRepository
import javax.inject.Inject

class GrabarClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(entidad: Cliente) = clienteRepository.grabar(entidad)
}