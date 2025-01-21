package com.example.z_ventapp.domain.usecase.empresa

import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.repository.EmpresaRepository
import javax.inject.Inject

class GrabarEmpresaUseCase @Inject constructor(
    private val repository: EmpresaRepository
) {

    suspend operator fun invoke(model: Empresa) = repository.grabar(model)

}