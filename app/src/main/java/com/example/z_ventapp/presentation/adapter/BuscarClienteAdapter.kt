package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.z_ventapp.databinding.ItemsBuscarClienteBinding
import com.example.z_ventapp.domain.model.Cliente

class BuscarClienteAdapter(
    private val listener: IOnClickListener
) : ListAdapter<Cliente, BuscarClienteAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickCliente(model: Cliente)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Cliente>() {
        override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsBuscarClienteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: Cliente) {
            binding.tvTitulo.text = model.nombre
            binding.tvNrodoc.text = model.nrodoc

            binding.itemView.setOnClickListener {
                listener.clickCliente(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsBuscarClienteBinding.inflate(
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