package com.example.z_ventapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.usecase.producto.ListarProductoUseCase
import com.example.z_ventapp.domain.usecase.producto.ObtenerProductoPorCodigoBarraUseCase
import com.example.z_ventapp.domain.usecase.ticket.GrabarTicketUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val grabarTicketUseCase: GrabarTicketUseCase,
    private val listarProductoUseCase: ListarProductoUseCase,
    private val obtenerProductoPorCodigoBarraUseCase: ObtenerProductoPorCodigoBarraUseCase
) : ViewModel() {

    // Observers

    // Listar Productos
    private val _uiStateListarProducto = MutableStateFlow<UiState<List<Producto>>?>(null)
    val uiStateListarProducto: StateFlow<UiState<List<Producto>>?> = _uiStateListarProducto
    // Listar Producto por Codigo Barra
    private val _uiStateCodigoBarra = MutableStateFlow<UiState<Producto?>?>(null)
    val uiStateCodigoBarra: StateFlow<UiState<Producto?>?> = _uiStateCodigoBarra
    // Almacenar Producto
    private val _itemProducto = MutableLiveData<Producto?>()
    val itemProducto: LiveData<Producto?> = _itemProducto
    // Cantidad de items en el carrito
    private val _totalItem = MutableLiveData<Int>()
    val totalItem: LiveData<Int> = _totalItem
    // Precio total de los items en el carrito
    private val _totalImporte = MutableLiveData<Double>()
    val totalImporte: LiveData<Double> = _totalImporte
    // Mensaje de producto no encontrado
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    // Resetear Lista de Productos
    fun resetUiStateListarProducto() {
        _uiStateListarProducto.value = null
    }

    fun resetUiStateCodigoBarra() {
        _uiStateCodigoBarra.value = null
    }

    // Listar Productos
    fun listarProducto(dato: String) = viewModelScope.launch {
        _uiStateListarProducto.value = UiState.Loading

        makeFlowCall { listarProductoUseCase(dato) }.collect{   // .collect --> Flow
            _uiStateListarProducto.value = it
        }
    }

    // Listar Producto por Codigo Barra
    fun buscarProductoPorCodigoBarra(codigoBarra: String) = viewModelScope.launch {
        _uiStateCodigoBarra.value = UiState.Loading

        makeCall { obtenerProductoPorCodigoBarraUseCase(codigoBarra) }.let { //  .let --> suspend function
            _uiStateCodigoBarra.value = it
        }
    }

}