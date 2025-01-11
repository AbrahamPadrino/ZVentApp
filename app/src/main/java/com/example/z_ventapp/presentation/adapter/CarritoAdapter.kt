package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsCarritoBinding
import com.example.z_ventapp.domain.model.DetalleTicket


import pe.pcs.libpcs.UtilsCommon

class CarritoAdapter(
    private val listener: IOnClickListener
) : RecyclerView.Adapter<CarritoAdapter.BindViewHolder>() {

    private var lista = listOf<DetalleTicket>()

    interface IOnClickListener {
        fun clickMas(model: DetalleTicket)
        fun clickMenos(model: DetalleTicket)
        fun clickEliminar(model: DetalleTicket)

    }

    inner class BindViewHolder(private val binding: ItemsCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: DetalleTicket) {
            binding.tvTitulo.text = model.descripcion
            binding.tvCantidad.text = model.cantidad.toString()
            binding.tvImporte.text =
                UtilsCommon.formatFromDoubleToString(model.precio * model.cantidad)

            binding.ibMenos.setOnClickListener {
                listener.clickMenos(model)
            }
            binding.ibMas.setOnClickListener {
                listener.clickMas(model)
            }
            binding.ibEliminar.setOnClickListener {
                listener.clickEliminar(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsCarritoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.enlazar(lista[position])
    }

    // Recibir lista DetalleTicket y Enviar al Adapter
    fun setData(_lista: List<DetalleTicket>) {
        lista = _lista
        notifyDataSetChanged()
    }
}