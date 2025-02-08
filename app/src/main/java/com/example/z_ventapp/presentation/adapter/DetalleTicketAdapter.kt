package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsDetalleTicketBinding
import com.example.z_ventapp.domain.model.ReporteDetalleTicket
import pe.pcs.libpcs.UtilsCommon

class DetalleTicketAdapter() :
    ListAdapter<ReporteDetalleTicket, DetalleTicketAdapter.BindViewHolder>(DiffCallback) {

    // calcular las diferencias entre la lista
    private object DiffCallback : DiffUtil.ItemCallback<ReporteDetalleTicket>() {
        override fun areItemsTheSame(
            oldItem: ReporteDetalleTicket,
            newItem: ReporteDetalleTicket
        ): Boolean {
            return oldItem.descripcion == newItem.descripcion
        }

        override fun areContentsTheSame(
            oldItem: ReporteDetalleTicket,
            newItem: ReporteDetalleTicket
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsDetalleTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: ReporteDetalleTicket) {
            binding.tvTitulo.text = model.descripcion
            binding.tvCantidad.text = "x ${model.cantidad}"
            binding.tvPrecio.text = UtilsCommon.formatFromDoubleToString(model.precio)
            binding.tvImporte.text = UtilsCommon.formatFromDoubleToString(model.importe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsDetalleTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.enlazar(getItem(position))
    }
}