package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsTicketBinding
import com.example.z_ventapp.domain.model.ReporteTicket
import pe.pcs.libpcs.UtilsCommon


class ReporteTicketAdapter(
    private val listener: IOnClickListener
) : ListAdapter<ReporteTicket, ReporteTicketAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickAnular(model: ReporteTicket)
        fun clickDetalle(model: ReporteTicket)
    }
    // calcular las diferencias entre la lista
    private object DiffCallback : DiffUtil.ItemCallback<ReporteTicket>() {
        override fun areItemsTheSame(oldItem: ReporteTicket, newItem: ReporteTicket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReporteTicket, newItem: ReporteTicket): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: ReporteTicket) {
            binding.tvTitulo.text = "Ticket #${model.id}"
            binding.tvFecha.text = model.fecha
            binding.tvCliente.text = model.cliente
            binding.tvTotal.text = UtilsCommon.formatFromDoubleToString(model.total)
            binding.tvEstado.text = model.estado

            binding.ibAnular.setOnClickListener {
                listener.clickAnular(model)
            }
            binding.ibDetalle.setOnClickListener {
                listener.clickDetalle(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsTicketBinding.inflate(
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