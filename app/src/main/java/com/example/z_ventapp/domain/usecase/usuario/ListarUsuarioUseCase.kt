package com.example.z_ventapp.domain.usecase.usuario

import com.example.z_ventapp.domain.repository.UsuarioRepository
import javax.inject.Inject

class ListarUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {

    operator fun invoke(dato: String) = usuarioRepository.listar(dato)

}