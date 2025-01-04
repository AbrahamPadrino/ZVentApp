package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsCatalogoBinding
import com.example.z_ventapp.databinding.ItemsProductoBinding
import com.example.z_ventapp.domain.model.Producto
import pe.pcs.libpcs.UtilsCommon

class CatalogoAdapter(
    private val listener: IOnClickListener
) : ListAdapter<Producto, CatalogoAdapter.BindViewHolder>(DiffCallback) {

    interface IOnClickListener {
        fun clickAgregar(model: Producto)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsCatalogoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: Producto) {
            binding.tvTitulo.text = model.descripcion
            binding.tvPrecio.text = UtilsCommon.formatFromDoubleToString(model.precio)

            binding.ibAgregar.setOnClickListener {
                listener.clickAgregar(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsCatalogoBinding.inflate(
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