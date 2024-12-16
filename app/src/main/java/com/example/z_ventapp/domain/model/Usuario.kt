package com.example.z_ventapp.domain.model

data class Usuario(
    var id: Int = 0,
    var nombre: String = "",
    var email: String = "",
    var clave: String = "",
    var vigente: Int = 0
)