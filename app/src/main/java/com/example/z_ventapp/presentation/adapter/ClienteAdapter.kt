package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ItemsPersonaBinding
import com.example.z_ventapp.domain.model.Cliente

class ClienteAdapter(
    private val listener: IOnClickListener
) : ListAdapter<Cliente, ClienteAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickEditar(model: Cliente)
        fun clickEliminar(model: Cliente)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Cliente>() {
        override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsPersonaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: Cliente) {
            binding.tvTitulo.text = model.nombre
            binding.tvDato.text = binding.root.context.getString(R.string.nrodoc)
            binding.tvEmail.text = model.nrodoc
            binding.tvEstado.text = if (model.vigente == 1)
                binding.root.context.getString(R.string.vigente)
            else binding.root.context.getString(R.string.no_vigente)

            binding.ibEditar.setOnClickListener {
                listener.clickEditar(model)
            }
            binding.ibEliminar.setOnClickListener {
                listener.clickEliminar(model)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsPersonaBinding.inflate(
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