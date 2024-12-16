package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.z_ventapp.databinding.ItemsPersonaBinding
import com.example.z_ventapp.domain.model.Usuario

class UsuarioAdapter(
    private val listener: IOnclickListener
) : ListAdapter<Usuario, UsuarioAdapter.BindViewHolder>(DiffCallback) {
    // para interactuar con los botenes internos (editar, borrar)
    interface IOnclickListener {
        fun clickEditar(usuario: Usuario)
        fun clickEliminar(usuario: Usuario)
    }
    // calcular las diferencias entre la lista
    private object DiffCallback : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Usuario, newItem: Usuario): Boolean {
            return oldItem == newItem
        }
    }
    inner class BindViewHolder(private val binding: ItemsPersonaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(usuario: Usuario) {
            binding.tvTitulo.text = usuario.nombre
            binding.tvDato.text = "Email"
            binding.tvEmail.text = usuario.email
            binding.tvEstado.text = if (usuario.vigente == 1) "Vigente" else "NO vigente"

            binding.ibEditar.setOnClickListener {
                listener.clickEditar(usuario)
            }
            binding.ibEliminar.setOnClickListener {
                listener.clickEliminar(usuario)
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