package com.example.z_ventapp.domain.model

data class DetalleTicket(
    var id: Int = 0,
    var cantidad: Int = 0,
    var precio: Double = 0.0,
    var importe: Double = 0.0,
    var idticket: Int = 0,
    var idproducto: Int = 0,
    var descripcion: String = ""
)