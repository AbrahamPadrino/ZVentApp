package com.example.z_ventapp.presentation.common

import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.model.Ticket
import pe.pcs.libpcs.UtilsCommon

object GenerarTicketRawBt {

    private const val TOT_CARACTER_LINEA: Int = 48
    private const val TOT_CARACTER_DETALLE: Int = 35

    fun comprobante(entidad: Ticket, empresa: Empresa): String {
        val data = StringBuilder().apply {
            append(centrarLinea(empresa.razonSocial.uppercase(), TOT_CARACTER_LINEA) + "\n")
            append(centrarLinea(empresa.ruc, TOT_CARACTER_LINEA) + "\n")
            append(centrarLinea(empresa.direccion, TOT_CARACTER_LINEA) + "\n\n")
            append(centrarLinea("TICKET: ${entidad.id}", TOT_CARACTER_LINEA) + "\n\n")

            append("-".repeat(TOT_CARACTER_LINEA) + "\n") //------------------------------------------------

            append("CLIENTE : ${entidad.cliente?.nombre}\n")
            append("FECHA   : ${entidad.fecha}\n\n")

            append("CANT DESCRIPCION ${" ".repeat(TOT_CARACTER_LINEA - 24)}IMPORTE\n")
            append("-".repeat(TOT_CARACTER_LINEA) + "\n")

            append(generarDetalle(entidad) + "\n")

            append("-".repeat(TOT_CARACTER_LINEA) + "\n")

            append(agregarTotales("TOTAL:", entidad.total, TOT_CARACTER_LINEA))
            append("\n\n\n\n")
        }

        android.util.Log.d("REPORT", data.toString())
        return data.toString()
    }

    private fun generarDetalle(ticket: Ticket): String {
        // joinToString: convierte una colección en una cadena, cada linea estara separado por un salto de linea
        return ticket.detalles.joinToString("\n") {
            agregaArticulo(
                it.cantidad,
                "${it.descripcion} x ${it.precio} c/u",
                it.importe
            )
        }
    }

    private fun agregaArticulo(
        cantidad: Int,
        descripcion: String,
        importe: Double
    ): String {
        val importeStr = UtilsCommon.formatFromDoubleToString(importe)

        if (cantidad.toString().length > 4 || importeStr.length > 7) {
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
                append("$cantidadStr${descripcionLines[0].padEnd(TOT_CARACTER_DETALLE)}$importeFormatted\n")

                // Agrega las líneas restantes de la descripción, excepto el primer elemento
                for (line in descripcionLines.drop(1)) {
                    append(" ".repeat(5) + line + "\n")
                }
            }.trimEnd()
        } else {
            "$cantidadStr${descripcion.padEnd(TOT_CARACTER_DETALLE)}$importeFormatted"
        }
    }

    private fun centrarLinea(texto: String, maxCaracteresPorLinea: Int): String {
        return buildString {
            // Dividir el texto en partes de tamaño maxCaracteresPorLinea
            texto.chunked(maxCaracteresPorLinea).forEach { linea ->
                // Calcular el espacio necesario para centrar
                val espacios = maxCaracteresPorLinea - linea.length
                val espacioIzquierdo = " ".repeat(espacios / 2)

                // Agregar la línea centrada al resultado
                appendLine("$espacioIzquierdo$linea")
            }
        }.trimEnd() // Eliminar el espacio en blanco al final
    }

    private fun agregarTotales(texto: String, valor: Double, totCaracteres: Int): String {
        val valorStr = UtilsCommon.formatFromDoubleToString(valor)
        val espacio = " ".repeat(totCaracteres - (texto.length + valorStr.length))
        return "$texto$espacio$valorStr"
    }
}