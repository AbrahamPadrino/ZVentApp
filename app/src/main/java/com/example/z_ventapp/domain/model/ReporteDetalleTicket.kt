package com.example.z_ventapp.domain.model

data class ReporteDetalleTicket(
    var descripcion: String = "",
    var precio: Double = 0.0,
    var cantidad: Int = 0,
    var importe: Double = 0.0
)
