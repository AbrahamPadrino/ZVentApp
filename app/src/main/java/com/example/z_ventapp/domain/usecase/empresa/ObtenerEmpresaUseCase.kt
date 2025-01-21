package com.example.z_ventapp.domain.usecase.empresa

import com.example.z_ventapp.domain.repository.EmpresaRepository
import javax.inject.Inject

class ObtenerEmpresaUseCase @Inject constructor(
    private val repository: EmpresaRepository
) {

    operator fun invoke() = repository.obtener()

}