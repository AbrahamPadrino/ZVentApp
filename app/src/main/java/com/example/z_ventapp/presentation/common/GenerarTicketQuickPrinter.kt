package com.example.z_ventapp.presentation.common

import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.model.Ticket
import pe.pcs.libpcs.UtilsCommon

object GenerarTicketQuickPrinter {

    private const val TOT_CARACTER_LINEA: Int = 48
    private const val TOT_CARACTER_DETALLE: Int = 35

    fun comprobante(aliasImpresora: String, entidad: Ticket, empresa: Empresa): String {
        val data = StringBuilder().apply {
            append("<PRINTER alias=$aliasImpresora>")
            append("<CENTER><BOLD>${empresa.razonSocial.uppercase()}<BR>")
            append("<CENTER><NORMAL>${empresa.ruc}<BR>")
            append("<CENTER>${empresa.direccion}<BR><BR>")
            append("<CENTER><BIG>TICKET: ${entidad.id}<BR><BR>")
            append("------------------------------------------------<BR>")
            append("CLIENTE : ${entidad.cliente?.nombre}<BR>")
            append("FECHA   : ${entidad.fecha}<BR><BR>")
            append("CANT DESCRIPCION ${" ".repeat(TOT_CARACTER_LINEA - 24)}IMPORTE<BR>")
            append("------------------------------------------------<BR>")
            append(generarDetalle(entidad) + "<BR>")
            append("------------------------------------------------<BR>")
            append(agregarTotales("TOTAL:", entidad.total, TOT_CARACTER_LINEA) + "<BR>")
            append("<CUT>")
        }

        android.util.Log.d("REPORT", data.toString())
        return data.toString()
    }

    private fun generarDetalle(ticket: Ticket): String {
        return ticket.detalles.joinToString("<BR>") {
            agregaArticulo("${it.descripcion} x ${it.precio} c/u", it.cantidad, it.importe)
        }
    }

    private fun agregaArticulo(
        descripcion: String,
        cantidad: Int,
        importe: Double
    ): String {
        val importeStr = UtilsCommon.formatFromDoubleToString(importe)

        if (cantidad.toString().length >= 5 || importeStr.length >= 8) {
            return "Los valores ingresados superan las columnas soportadas."
        }

        val cantidadStr = cantidad.toString().padEnd(5)
        val importeFormatted = importeStr.padStart(8)

        return if (descripcion.length > TOT_CARACTER_DETALLE) {
            // Crea una lista de líneas de TOT_CARACTER_DETALLE caracteres cada una
            val descripcionLines = descripcion.chunked(TOT_CARACTER_DETALLE)

            // Construye la cadena final
            buildString {
                // Agrega la primera línea de la descripción
                append("$cantidadStr${descripcionLines[0].padEnd(TOT_CARACTER_DETALLE)}$importeFormatted<BR>")

                // Agrega las líneas restantes de la descripción, excepto el primer elemento
                for (line in descripcionLines.drop(1)) {
                    append(" ".repeat(5) + line + "<BR>")
                }
            }.trimEnd()
        } else {
            "$cantidadStr${descripcion.padEnd(TOT_CARACTER_DETALLE)}$importeFormatted"
        }
    }

    private fun agregarTotales(texto: String, valor: Double, totCaracteres: Int): String {
        val valorStr = UtilsCommon.formatFromDoubleToString(valor)
        val espacio = " ".repeat(totCaracteres - (texto.length + valorStr.length))
        return "$texto$espacio$valorStr"
    }
}