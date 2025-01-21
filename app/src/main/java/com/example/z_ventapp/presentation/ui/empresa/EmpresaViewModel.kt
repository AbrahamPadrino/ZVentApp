package com.example.z_ventapp.presentation.ui.empresa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.usecase.empresa.GrabarEmpresaUseCase
import com.example.z_ventapp.domain.usecase.empresa.ObtenerEmpresaUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpresaViewModel @Inject constructor(
    private val grabarEmpresaUseCase: GrabarEmpresaUseCase,
    private val obtenerEmpresaUseCase: ObtenerEmpresaUseCase
) : ViewModel() {
    // Manejador de estados
    private val _uiStateGrabar = MutableStateFlow<UiState<Unit>?>(null)
    val uiStateGrabar : StateFlow<UiState<Unit>?> = _uiStateGrabar

    private val _uiStateObtener = MutableStateFlow<UiState<Empresa?>?>(null)
    val uiStateObtener : StateFlow<UiState<Empresa?>?> = _uiStateObtener

   // Reseters de estados
    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun resetUiStateObtener() {
        _uiStateObtener.value = null
    }

    // Function
    fun grabar(empresa: Empresa) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { grabarEmpresaUseCase(empresa) }.let {
            _uiStateGrabar.value = it
        }
    }

    fun obtenerEmpresa() = viewModelScope.launch {
        _uiStateObtener.value = UiState.Loading

        makeFlowCall { obtenerEmpresaUseCase() }.collect {
            _uiStateObtener.value = it
        }
    }

}