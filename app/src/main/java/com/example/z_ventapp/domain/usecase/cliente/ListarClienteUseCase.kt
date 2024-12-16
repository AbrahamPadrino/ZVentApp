package com.example.z_ventapp.domain.usecase.cliente

import com.example.z_ventapp.domain.repository.ClienteRepository
import javax.inject.Inject

class ListarClienteUseCase @Inject constructor(
    private val clienteRepository: ClienteRepository
) {
    operator fun invoke(dato: String) = clienteRepository.listar(dato)
}