package com.example.z_ventapp.presentation.ui.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.usecase.producto.EliminarProductoUseCase
import com.example.z_ventapp.domain.usecase.producto.ListarProductoUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val listarProductoUseCase: ListarProductoUseCase,
    private val eliminarProductoUseCase: EliminarProductoUseCase
) : ViewModel() {

    private val _uiStateListar = MutableStateFlow<UiState<List<Producto>>>(UiState.Loading)
    val uiStateListar: StateFlow<UiState<List<Producto>>> = _uiStateListar

    private val _uiStateEliminar = MutableStateFlow<UiState<Int>?>(null)
    val uiStateEliminar: StateFlow<UiState<Int>?> = _uiStateEliminar

    // Porque ListarProductoUseCase retorna un flow
    private val _datoBuscado = MutableStateFlow("")

    init {
        _datoBuscado
            .flatMapLatest { dato ->
                flow {
                    emit(UiState.Loading)
                    emitAll(
                        makeFlowCall {
                            listarProductoUseCase(dato).map {
                                it.ifEmpty { emptyList() }
                            }
                        }
                    )
                }
            }
            .onEach { _uiStateListar.value = it }
            .launchIn(viewModelScope)
    }

    fun buscarPorDato(dato: String) {
        _datoBuscado.value = dato
    }

    fun resetUiStateEliminar() {
        _uiStateEliminar.value = null
    }

    // Porque EliminarProductoUseCase retorna un suspend function
    fun eliminar(entidad: Producto) = viewModelScope.launch {
        _uiStateEliminar.value = UiState.Loading

        makeCall { eliminarProductoUseCase(entidad) }.let {
            _uiStateEliminar.value = it
        }
    }
}