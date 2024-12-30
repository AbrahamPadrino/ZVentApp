package com.example.z_ventapp.domain.usecase.ticket

import com.example.z_ventapp.domain.model.Ticket
import com.example.z_ventapp.domain.repository.TicketRepository
import javax.inject.Inject

class AnularTicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository
) {
    suspend operator fun invoke(idticket: Int) = ticketRepository.anularTicket(idticket)
}