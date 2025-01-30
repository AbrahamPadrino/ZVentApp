package com.example.z_ventapp.domain.usecase.ticket

import com.example.z_ventapp.domain.repository.TicketRepository
import javax.inject.Inject

class ReporteCajaUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    operator fun invoke(desde: String, hasta: String) =
        ticketRepository.reporteCajaPorFecha(desde, hasta)
}