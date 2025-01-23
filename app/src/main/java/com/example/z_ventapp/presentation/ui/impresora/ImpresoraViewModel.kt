package com.example.z_ventapp.presentation.ui.impresora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.domain.usecase.impresora.GrabarImpresoraUseCase
import com.example.z_ventapp.domain.usecase.impresora.ObtenerImpresoraUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImpresoraViewModel @Inject constructor(
    private val grabarImpresoraUseCase: GrabarImpresoraUseCase,
    private val obtenerImpresoraUseCase: ObtenerImpresoraUseCase
) : ViewModel() {
    // Manejadores de estado.
    private val _uiStateGrabar = MutableStateFlow<UiState<Unit>?>(null)
    val uiStateGrabar: StateFlow<UiState<Unit>?> = _uiStateGrabar

    private val _uiStateObtener = MutableStateFlow<UiState<Impresora?>?>(null)
    val uiStateObtener: StateFlow<UiState<Impresora?>?> = _uiStateObtener

    // Rerseteador de estado.
    fun resetUiStateGrabar() {
        _uiStateGrabar.value = null
    }

    fun resetUiStateObtener() {
        _uiStateObtener.value = null
    }

    // Funciones
    fun grabar(model: Impresora) = viewModelScope.launch {
        _uiStateGrabar.value = UiState.Loading

        makeCall { grabarImpresoraUseCase(model) }.let {
            _uiStateGrabar.value = it
        }
    }

    fun obtener() = viewModelScope.launch {
        _uiStateObtener.value = UiState.Loading

        makeFlowCall { obtenerImpresoraUseCase() }.collect {
            _uiStateObtener.value = it
        }
    }

}