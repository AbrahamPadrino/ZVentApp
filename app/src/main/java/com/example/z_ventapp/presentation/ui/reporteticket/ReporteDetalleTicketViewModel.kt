package com.example.z_ventapp.presentation.ui.reporteticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.ReporteDetalleTicket
import com.example.z_ventapp.domain.model.ReporteTicket
import com.example.z_ventapp.domain.usecase.ticket.ObtenerTicketPorIdUseCase
import com.example.z_ventapp.domain.usecase.ticket.ReporteDetalleTicketUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReporteDetalleTicketViewModel @Inject constructor(
    private val reporteDetalleTicketUseCase: ReporteDetalleTicketUseCase,
    private val obtenerTicketPorIdUseCase: ObtenerTicketPorIdUseCase

) : ViewModel() {

    private val _uiStateDetalle = MutableStateFlow<UiState<List<ReporteDetalleTicket>>?>(null)
    val uiStateDetalle = _uiStateDetalle.asStateFlow()

    private val _uiStateObtener = MutableStateFlow<UiState<ReporteTicket?>?>(null)
    val uiStateObtener = _uiStateObtener.asStateFlow()

    fun resetUiStateDetalle() {
        _uiStateDetalle.value = null
    }

    fun resetUiStateObtener() {
        _uiStateObtener.value = null
    }

    fun obtenerTicketPorId(id: Int) = viewModelScope.launch {
        _uiStateObtener.value = UiState.Loading

        makeCall { obtenerTicketPorIdUseCase(id) }.let {
            _uiStateObtener.value = it
        }
    }

    fun reporteDetalleTicket(id: Int) = viewModelScope.launch {
        _uiStateDetalle.value = UiState.Loading

        makeCall { reporteDetalleTicketUseCase(id) }.let {
            _uiStateDetalle.value = it
        }
    }

}