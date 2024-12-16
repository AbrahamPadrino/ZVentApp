package com.example.z_ventapp.domain.usecase.usuario

import com.example.z_ventapp.domain.repository.UsuarioRepository
import javax.inject.Inject

class ExisteCuentaUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {

    suspend operator fun invoke() = usuarioRepository.existeCuenta()

}