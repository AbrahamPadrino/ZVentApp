package com.example.z_ventapp.domain.usecase.ticket

import com.example.z_ventapp.domain.repository.TicketRepository
import javax.inject.Inject

class ReporteTicketMensualUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(anio: String) = ticketRepository.reporteTicketMensual(anio)
}