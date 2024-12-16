package com.example.z_ventapp.presentation.ui.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.usecase.cliente.EliminarClienteUseCase
import com.example.z_ventapp.domain.usecase.cliente.ListarClienteUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val listarClienteUseCase: ListarClienteUseCase,
    private val eliminarClienteUseCase: EliminarClienteUseCase
) : ViewModel() {
    // Modificadores de estado
    private val _uiStateListar = MutableStateFlow<UiState<List<Cliente>>?>(null)
    val uiStateListar: StateFlow<UiState<List<Cliente>>?> = _uiStateListar

    private val _uiStateEliminar = MutableStateFlow<UiState<Int>?>(null)
    val uiStateEliminar: StateFlow<UiState<Int>?> = _uiStateEliminar

    //
    fun resetUiStateListar() {
        _uiStateListar.value = null
    }

    fun resetUiStateEliminar() {
        _uiStateEliminar.value = null
    }

    // Funciones
    fun listar(dato: String) = viewModelScope.launch {
        _uiStateListar.value = UiState.Loading

        listarClienteUseCase(dato)
            .flowOn(Dispatchers.IO)
            .catch {
                _uiStateListar.value = UiState.Error(it.message.orEmpty())
            }
            .collect {
                _uiStateListar.value = UiState.Success(it)
            }
    }

    fun eliminar(entidad: Cliente) = viewModelScope.launch {
        _uiStateEliminar.value = UiState.Loading

        makeCall { eliminarClienteUseCase(entidad) }.let {
            _uiStateEliminar.value = it
        }
    }


}