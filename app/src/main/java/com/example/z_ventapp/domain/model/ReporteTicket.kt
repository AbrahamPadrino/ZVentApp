package com.example.z_ventapp.domain.model
data class ReporteTicket(
    var id: Int = 0,
    var fecha: String = "",
    var total: Double = 0.0,
    var estado: String = "",
    var cliente: String = ""
)
