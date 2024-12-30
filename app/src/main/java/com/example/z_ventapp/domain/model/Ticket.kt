package com.example.z_ventapp.domain.model

data class Ticket(
    var id: Int=0,
    var fecha: String="",
    var total: Double=0.0,
    var estado: String="",
    var idusuario: Int=0,
    var detalles: List<DetalleTicket> = emptyList(),
    var cliente: Cliente? = null
)