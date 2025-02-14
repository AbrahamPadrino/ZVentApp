package com.example.z_ventapp.presentation.common

import android.content.Context
import android.content.Intent
import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.domain.model.Ticket
import pe.pcs.libpcs.UtilsMessage

fun enviarTicketImpresora(
    context: Context,
    entidad: Ticket,
    empresa: Empresa,
    tipoImpresora: Impresora,
    alias: String? = null
) {
    try {
        val intent = Intent().apply {
            type = "text/plain"

            val texto = when (tipoImpresora.tipo) {
                ConstantsApp.DRIVER_RAW_BT -> GenerarTicketRawBt.comprobante(entidad, empresa)
                ConstantsApp.DRIVER_QUICK_PRINTER -> GenerarTicketQuickPrinter.comprobante(
                    alias ?: "",
                    entidad,
                    empresa
                )

                else -> { "" }
            }

            putExtra(Intent.EXTRA_TEXT, texto)

            when (tipoImpresora.tipo) {
                ConstantsApp.DRIVER_RAW_BT -> {
                    action = Intent.ACTION_SEND
                    setPackage("ru.a402d.rawbtprinter")
                }

                ConstantsApp.DRIVER_QUICK_PRINTER -> action = "pe.diegoveloper.printing"
            }
        }

        context.startActivity(intent)
    } catch (ex: Exception) {
        UtilsMessage.showAlertOk("ERROR Impresora", ex.message.orEmpty(), context)
    }
}