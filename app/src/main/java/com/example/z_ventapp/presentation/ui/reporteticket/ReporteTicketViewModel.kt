package com.example.z_ventapp.presentation.ui.reporteticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_ventapp.domain.model.ReporteTicket
import com.example.z_ventapp.domain.usecase.ticket.AnularTicketUseCase
import com.example.z_ventapp.domain.usecase.ticket.ReporteTicketUseCase
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.makeCall
import com.example.z_ventapp.presentation.common.makeFlowCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReporteTicketViewModel @Inject constructor(
    private val reporteTicketUseCase: ReporteTicketUseCase,
    private val anularTicketUseCase: AnularTicketUseCase
    ) : ViewModel() {

    private val _uiStateReporte = MutableStateFlow<UiState<List<ReporteTicket>>?>(null)
    val uiStateReporte = _uiStateReporte.asStateFlow()

    private val _uiStateAnular = MutableStateFlow<UiState<Int>?>(null)
    val uiStateAnular = _uiStateAnular.asStateFlow()

    fun resetUiStateReporte() {
        _uiStateReporte.value = null
    }

    fun resetUiStateAnular() {
        _uiStateAnular.value = null
    }

    fun reporteTicketPorFecha(desde: String, hasta: String) = viewModelScope.launch {
        _uiStateReporte.value = UiState.Loading

        makeFlowCall { reporteTicketUseCase(desde, hasta) }.collect {
            _uiStateReporte.value = it
        }
    }

    fun anularTicket(idticket: Int) = viewModelScope.launch {
        _uiStateAnular.value = UiState.Loading

        makeCall { anularTicketUseCase(idticket) }.let {
            _uiStateAnular.value = it
        }
    }

}