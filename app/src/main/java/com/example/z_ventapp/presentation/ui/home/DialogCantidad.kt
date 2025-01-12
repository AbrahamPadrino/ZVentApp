package com.example.z_ventapp.presentation.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.DialogFragment
import com.example.z_ventapp.databinding.DialogCantidadBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import pe.pcs.libpcs.UtilsCommon

class DialogCantidad : DialogFragment() {

    private lateinit var binding: DialogCantidadBinding
    private var titulo: String = ""
    private var mensaje: String = ""
    private var precio: Double = 0.0
    private lateinit var iOnClickListener: IOnClickListener


    interface IOnClickListener {
        fun eviarItem(cantidad: Int, precio: Double)
    }

    companion object {
        private const val DEFAULT_CANTIDAD = 1

        fun newinstance(
            titulo: String,
            mensaje: String,
            precio: Double,
            iOnClickListener: IOnClickListener
        ): DialogCantidad {
            return DialogCantidad().apply {
                this.titulo = titulo
                this.mensaje = mensaje
                this.precio = precio
                this.iOnClickListener = iOnClickListener
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCantidadBinding.inflate(layoutInflater).apply {
            etPrecio.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            etPrecio.setText(UtilsCommon.formatFromDoubleToString(precio))

            ibMenos.setOnClickListener { actualizarCantidad(-1) }
            ibMas.setOnClickListener { actualizarCantidad(1) }
        }

        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setView(binding.root)
            setTitle(titulo)
            setMessage(mensaje)
            setCancelable(false)

            setPositiveButton("Aceptar") { dialog, _ ->
                val cantidad = binding.tvCantidad.text.toString().toIntOrNull() ?: DEFAULT_CANTIDAD
                val precio =
                    binding.etPrecio.text.toString().toDoubleOrNull() ?: this@DialogCantidad.precio

                iOnClickListener.eviarItem(cantidad, precio)
                dialog.dismiss()
            }

            setNegativeButton("Cancelar") { dialog, _ ->
                iOnClickListener.eviarItem(0, 0.0)
                dialog.dismiss()

            }
        }.create()

        builder.setCancelable(false) // para que no se pueda cerrar con el boton de atras
        builder.setCanceledOnTouchOutside(false) // para que no se pueda cerrar con tocar fuera

        return builder
    }

    @SuppressLint("SetTextI18n")
    private fun actualizarCantidad(cantidad: Int) {
        val nuevaCantidad = (binding.tvCantidad.text.toString().toIntOrNull()
            ?: DEFAULT_CANTIDAD) + cantidad

        if (nuevaCantidad > 0)
            binding.tvCantidad.text = nuevaCantidad.toString()
    }
}