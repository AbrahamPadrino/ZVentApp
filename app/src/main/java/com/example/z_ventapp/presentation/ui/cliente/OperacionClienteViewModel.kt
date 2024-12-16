package com.example.z_ventapp.presentation.ui.cliente

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.usecase.cliente.GrabarClienteUseCase
import com.example.z_ventapp.domain.usecase.cliente.ObtenerClientePorIdUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperacionClienteViewModel @Inject constructor(
    private val grabarClienteUseCase: GrabarClienteUseCase,
    private val obtenerClientePorIdUseCase: ObtenerClientePorIdUseCase
): ViewModel() {

    // Manejadores de estado
    private val _uiStateGrabar = MutableLiveData<UiState<Int>?>()
    val uiStateGrabar: LiveData<UiState<Int>?> = _uiStateGrabar

    private val _uiStateObtener = MutableLiveData<UiState<Cliente?>?>()
    val uiStateObtener: LiveData<UiState<Cliente?>?> = _uiStateObtener
    //
    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun resetUiStateObtener() {
        _uiStateObtener.value = null
    }
    // Funciones
    fun grabar(cliente: Cliente) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { grabarClienteUseCase(cliente) }.let {
            _uiStateGrabar.value = it
        }
    }

    fun obtenerCliente(id: Int) = viewModelScope.launch {
        _uiStateObtener.value = UiState.Loading

        makeCall { obtenerClientePorIdUseCase(id) }.let {
            _uiStateObtener.value = it
        }
    }
    //
}