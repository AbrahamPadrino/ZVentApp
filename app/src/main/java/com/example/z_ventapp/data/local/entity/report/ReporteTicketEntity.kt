package com.example.z_ventapp.data.local.entity.report

data class ReporteTicketEntity(
    var id: Int = 0,
    var fecha: String = "",
    var total: Double = 0.0,
    var estado: String = "",
    var cliente: String = ""
)
