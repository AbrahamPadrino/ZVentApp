package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsProductoBinding
import com.example.z_ventapp.domain.model.Producto
import pe.pcs.libpcs.UtilsCommon

class ProductoAdapter(
    private val listener: IOnClickListener
) : ListAdapter<Producto, ProductoAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickEditar(model: Producto)
        fun clickEliminar(model: Producto)

    }

    private object DiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: Producto) {
            binding.tvTitulo.text = model.descripcion
            binding.tvCodigoBarra.text = model.codigobarra
            binding.tvPrecio.text = UtilsCommon.formatFromDoubleToString(model.precio)

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
            ItemsProductoBinding.inflate(
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