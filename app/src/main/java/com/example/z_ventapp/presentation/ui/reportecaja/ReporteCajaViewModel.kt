package com.example.z_ventapp.presentation.ui.reportecaja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.ReporteCaja
import com.example.z_ventapp.domain.usecase.ticket.ReporteCajaUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReporteCajaViewModel @Inject constructor(
    private val reporteCajaUseCase: ReporteCajaUseCase
): ViewModel() {
    // Manejador de Estado de la UI
    private val _uiStateCaja = MutableStateFlow<UiState<List<ReporteCaja>>?>(null)
    val uiStateCaja = _uiStateCaja.asStateFlow()
    // Reseteador de Estado.
    fun resetUiStateCaja() {
        _uiStateCaja.value = null
    }
    // Función para obtener el reporte de caja
    fun reporteCaja(desde: String, hasta: String) = viewModelScope.launch {
        _uiStateCaja.value = UiState.Loading

        makeFlowCall { reporteCajaUseCase(desde, hasta) }.collect {
            _uiStateCaja.value = it
        }
    }
}