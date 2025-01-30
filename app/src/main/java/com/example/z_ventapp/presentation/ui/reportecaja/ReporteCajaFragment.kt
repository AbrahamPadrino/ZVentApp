package com.example.z_ventapp.presentation.ui.reportecaja

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentReporteCajaBinding
import com.example.z_ventapp.presentation.adapter.ReporteCajaAdapter
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsDate
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ReporteCajaFragment : Fragment() {

    private lateinit var binding: FragmentReporteCajaBinding
    private val viewModel: ReporteCajaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReporteCajaBinding.inflate(inflater, container, false)
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
        // Establecer el Icono de Fecha
        binding.tilDesde.startIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.baseline_date_range_24
        )

        binding.tilHasta.startIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.baseline_date_range_24
        )

        // Preparar el recycler.
        binding.rvLista.apply {
            adapter = ReporteCajaAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
        // Seleccionar la fecha
        binding.etDesde.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etDesde, parentFragmentManager)
        }

        binding.etHasta.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etHasta, parentFragmentManager)
        }
        // Para Buscar por fecha
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

                viewModel.uiStateCaja.collect {
                    when (it) {
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            UtilsMessage.showAlertOk(
                                "Error", it.message, requireContext()
                            )
                            viewModel.resetUiStateCaja()
                        }

                        UiState.Loading -> binding.progressBar.isVisible = true
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            (binding.rvLista.adapter as ReporteCajaAdapter).submitList(it.data)
                            viewModel.resetUiStateCaja()
                        }

                        null -> Unit
                    }
                }
            }
        }
    }

    private fun buscarPorFechas() {
        if (binding.etDesde.text.toString().trim().isEmpty() || binding.etHasta.text.toString()
                .trim().isEmpty()
        ) return

        viewModel.reporteCaja(
            binding.etDesde.text.toString().trim(),
            binding.etHasta.text.toString().trim()
        )
    }

}