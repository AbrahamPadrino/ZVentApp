package com.example.z_ventapp.presentation.ui.producto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.domain.usecase.producto.GrabarProductoUseCase
import com.example.z_ventapp.domain.usecase.producto.ObtenerProductoPorIdUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperacionProductoViewModel @Inject constructor(
    private val grabarProductoUseCase: GrabarProductoUseCase,
    private val obtenerProductoPorIdUseCase: ObtenerProductoPorIdUseCase
) : ViewModel() {

    // _uiStateGrabar solo se podra modificar dentro del ViewModel, su estado inicial sera null
    private val _uiStateGrabar = MutableLiveData<UiState<Int>?>(null)
    // uiStateGrabar sera publico, solo se podra leer y obtener su estado desde fuera del ViewModel
    val uiStateGrabar: LiveData<UiState<Int>?> = _uiStateGrabar

    private val _uiStateObtener = MutableLiveData<UiState<Producto?>?>(null)
    val uiStateObtener: LiveData<UiState<Producto?>?> = _uiStateObtener

    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun resetUiStateObtener() {
        _uiStateObtener.value = null
    }

    fun grabar(entidad: Producto) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { grabarProductoUseCase(entidad) }.let {
            _uiStateGrabar.value = it
        }
    }

    fun obtener(id: Int) = viewModelScope.launch {
        _uiStateObtener.value = UiState.Loading

        makeCall { obtenerProductoPorIdUseCase(id) }.let {
            _uiStateObtener.value = it
        }
    }
}