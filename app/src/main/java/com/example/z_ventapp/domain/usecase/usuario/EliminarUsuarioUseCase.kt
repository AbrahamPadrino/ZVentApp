package com.example.z_ventapp.domain.usecase.usuario

import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.domain.repository.UsuarioRepository
import javax.inject.Inject

class EliminarUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario) = usuarioRepository.eliminar(usuario)

}