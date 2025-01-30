package com.example.z_ventapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.z_ventapp.databinding.ItemsReporteCajaBinding
import com.example.z_ventapp.domain.model.ReporteCaja
import pe.pcs.libpcs.UtilsCommon


class ReporteCajaAdapter() :
    ListAdapter<ReporteCaja, ReporteCajaAdapter.BindViewHolder>(DiffCallback) {
    // calcular las diferencias entre la lista
    private object DiffCallback : DiffUtil.ItemCallback<ReporteCaja>() {
        override fun areItemsTheSame(oldItem: ReporteCaja, newItem: ReporteCaja): Boolean {
            return oldItem.usuario == newItem.usuario
        }

        override fun areContentsTheSame(oldItem: ReporteCaja, newItem: ReporteCaja): Boolean {
            return oldItem == newItem
        }
    }

    inner class BindViewHolder(private val binding: ItemsReporteCajaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun enlazar(model: ReporteCaja) {
            binding.tvTitulo.text = model.usuario
            binding.tvTotal.text = UtilsCommon.formatFromDoubleToString(model.total)
            binding.tvFecha.text = model.fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        return BindViewHolder(
            ItemsReporteCajaBinding.inflate(
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