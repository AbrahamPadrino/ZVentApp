package com.example.z_ventapp.presentation.ui.reporteticket

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentReporteTicketBinding
import com.example.z_ventapp.domain.model.ReporteTicket
import com.example.z_ventapp.presentation.adapter.ReporteTicketAdapter
import com.example.z_ventapp.presentation.common.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsDate
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ReporteTicketFragment : Fragment(), ReporteTicketAdapter.IOnClickListener {

    private lateinit var binding: FragmentReporteTicketBinding
    private val viewModel: ReporteTicketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReporteTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        UtilsDate.mostrarFechaActual(binding.etDesde)
        UtilsDate.mostrarFechaActual(binding.etHasta)
    }

    private fun initListener() {

        binding.rvLista.apply {
            adapter = ReporteTicketAdapter(this@ReporteTicketFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.etDesde.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etDesde, parentFragmentManager)
        }

        binding.etHasta.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etHasta, parentFragmentManager)
        }

        binding.etDesde.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                buscarPorFechas()
            }
        })

        binding.etHasta.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                buscarPorFechas()
            }
        })

    }

    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiStateReporte.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "Error", it.message, requireContext()
                                )
                                viewModel.resetUiStateReporte()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                (binding.rvLista.adapter as ReporteTicketAdapter).submitList(it.data)
                                viewModel.resetUiStateReporte()
                            }

                            null -> Unit
                        }
                    }

                }

                launch {
                    viewModel.uiStateAnular.collect{
                        when(it){
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "Error", it.message, requireContext()
                                )
                                viewModel.resetUiStateAnular()
                            }
                            UiState.Loading -> binding.progressBar.isVisible = true

                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                if(it.data < 1) return@collect

                                UtilsMessage.showToast(
                                    requireContext(), "Ticket anulado correctamente"
                                )
                                viewModel.resetUiStateAnular()
                            }
                            null -> Unit
                        }
                    }
                }
            }
        }

    }

    override fun clickAnular(model: ReporteTicket) {
        if(model.estado.trim().lowercase() == "anulado") {
            UtilsMessage.showToast(requireContext(), "Ticket ya anulado")
            return
        }

        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("Anular Ticket")
            setMessage("¿Desea anular el ticket # ${model.id}?")

            setPositiveButton("SI"){ dialog, _ ->
                viewModel.anularTicket(model.id)
                dialog.dismiss()
            }

            setNegativeButton("NO"){ dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    override fun clickDetalle(model: ReporteTicket) {

    }

    private fun buscarPorFechas() {
        if (binding.etDesde.text.toString().trim().isEmpty() || binding.etHasta.text.toString()
                .trim().isEmpty()
        ) return

        viewModel.reporteTicketPorFecha(
            binding.etDesde.text.toString().trim(),
            binding.etHasta.text.toString().trim()
        )
    }


}