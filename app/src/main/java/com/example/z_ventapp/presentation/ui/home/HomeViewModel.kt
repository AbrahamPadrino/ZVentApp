package com.example.z_ventapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.DetalleTicket
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.model.Ticket
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

    //*** Inicio para Catalogo Fragment ***//
    // Observers
    // Listar Productos
    private val _uiStateListarProducto = MutableStateFlow<UiState<List<Producto>>?>(null)
    val uiStateListarProducto: StateFlow<UiState<List<Producto>>?> = _uiStateListarProducto
    // Listar Producto por Codigo Barra
    private val _uiStateCodigoBarra = MutableStateFlow<UiState<Producto?>?>(null)
    val uiStateCodigoBarra: StateFlow<UiState<Producto?>?> = _uiStateCodigoBarra
    // Almacenar Producto
    private val _itemProducto = MutableLiveData<Producto?>()
    // Cantidad de items en el carrito
    private val _totalItem = MutableLiveData<Int>()
    val totalItem: LiveData<Int> = _totalItem
    // Mensaje de producto no encontrado
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    // Reseters
    // Resetear Lista de Productos
    fun resetUiStateListarProducto() {
        _uiStateListarProducto.value = null
    }

    fun resetUiStateCodigoBarra() {
        _uiStateCodigoBarra.value = null
    }
    // Functions
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

    fun limpiarMensaje() {
        _mensaje.value = ""
    }

    fun asignarProducto(item: Producto?) {
        _itemProducto.value = item
    }

    private fun actualizarTotales() {
        _totalItem.value = _listaCarrito.value?.sumOf { it.cantidad }
        _totalImporte.value = _listaCarrito.value?.sumOf { it.cantidad * it.precio }
    }

    fun agregarProductoCarrito(cantidad: Int, precio: Double) {
        // Validar carrito vacio
        if (cantidad == 0 || precio == 0.0 || _itemProducto.value == null) return
        // Validar si el producto ya se encuentra en el carrito
        _listaCarrito.value?.find {
            it.idproducto == _itemProducto.value!!.id
        }?.let {
            _mensaje.value = "El producto ya se encuentra en el carrito"
            return
        }
        // Agregar producto al carrito
        _listaCarrito.value?.add(
            DetalleTicket().apply {
                idproducto = _itemProducto.value!!.id
                descripcion = _itemProducto.value!!.descripcion
                this.cantidad = cantidad
                this.precio = precio
                importe = precio * cantidad
            }
        )

        _listaCarrito.value = _listaCarrito.value

        actualizarTotales()
        asignarProducto(null)
    }

    /**
     *
     */

    //*** Inicio para Home Fragment ***//
    // Precio total de los items en el carrito
    // Observers
    private val _totalImporte = MutableLiveData<Double>()
    val totalImporte: LiveData<Double> = _totalImporte
    // Lista de carrito
    private val _listaCarrito = MutableLiveData<MutableList<DetalleTicket>>(mutableListOf())
    val listaCarrito: LiveData<MutableList<DetalleTicket>> = _listaCarrito
    // Manejador de Estado
    private val _uiStateGrabarTicket = MutableLiveData<UiState<Int>?>(null)
    val uiStateGrabarTicket: LiveData<UiState<Int>?> = _uiStateGrabarTicket

    // Reseters
    // Resetear Estado
    fun resetUiStateGrabarTicket() {
        _uiStateGrabarTicket.value = null
    }

    // Functions
    fun quitarProductoCarrito(model: DetalleTicket) {
        _listaCarrito.value?.remove(model)
        _listaCarrito.value = _listaCarrito.value
        actualizarTotales()
    }

    fun limpiarCarrito() {
        _listaCarrito.value?.clear()
        _listaCarrito.value = _listaCarrito.value
        actualizarTotales()
    }

    fun aumentarCantidadProducto(model: DetalleTicket) {
        _listaCarrito.value?.find {
            it.idproducto == model.idproducto
        }?.let {
            it.cantidad += 1 // ++
            it.importe = it.cantidad * it.precio
            actualizarTotales()
        }
        _listaCarrito.value = _listaCarrito.value
    }

    fun disminuirCantidadProducto(model: DetalleTicket) {
        _listaCarrito.value?.find {
            it.idproducto == model.idproducto && it.cantidad > 0
        }?.let {
            it.cantidad-- // -=
            it.importe = it.cantidad * it.precio
            actualizarTotales()
        }
        _listaCarrito.value = _listaCarrito.value
    }

    fun grabarTicket(model: Ticket) = viewModelScope.launch {
        _uiStateGrabarTicket.value = UiState.Loading

        makeCall { grabarTicketUseCase(model) }.let {
            _uiStateGrabarTicket.value = it
        }

    }


}