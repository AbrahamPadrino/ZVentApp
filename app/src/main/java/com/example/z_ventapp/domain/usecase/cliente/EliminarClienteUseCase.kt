package com.example.z_ventapp.domain.usecase.cliente

import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.repository.ClienteRepository
import javax.inject.Inject

class EliminarClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    suspend operator fun invoke(entidad: Cliente) = clienteRepository.eliminar(entidad)
}