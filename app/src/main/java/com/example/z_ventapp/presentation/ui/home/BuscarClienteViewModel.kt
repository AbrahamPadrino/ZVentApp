package com.example.z_ventapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.domain.usecase.cliente.ListarClienteUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuscarClienteViewModel @Inject constructor(
    private val listarClienteUseCase: ListarClienteUseCase
): ViewModel(){

    private val _itemCliente = MutableStateFlow<Cliente?>(null)
    val itemCliente : StateFlow<Cliente?> = _itemCliente

    private val _uiStateCliente = MutableStateFlow<UiState<List<Cliente>>?>(null)
    val uiStateCliente : StateFlow<UiState<List<Cliente>>?> = _uiStateCliente

    fun asignarCliente(item: Cliente?){
        _itemCliente.value = item
    }

    fun resetUiStateCliente(){
        _uiStateCliente.value=null
    }

    fun listarCliente(dato: String) = viewModelScope.launch {
        _uiStateCliente.value = UiState.Loading

        makeFlowCall { listarClienteUseCase(dato) }.collect{
            _uiStateCliente.value = it

        }
    }
}