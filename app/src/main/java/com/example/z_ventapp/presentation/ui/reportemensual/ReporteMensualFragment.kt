package com.example.z_ventapp.presentation.ui.reportemensual

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
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentReporteMensualBinding
import com.example.z_ventapp.domain.model.ReporteTicketMensual
import com.example.z_ventapp.presentation.common.UiState
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.journeyapps.barcodescanner.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.NumberPickerDialog
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsDate
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ReporteMensualFragment : Fragment() {

    private lateinit var binding: FragmentReporteMensualBinding
    private val viewModel: ReporteMensualViewModel by viewModels()
    private val entries = ArrayList<BarEntry>() // Lista para almacenar los datos de la gráfica


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReporteMensualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        UtilsDate.mostrarAnioActual(binding.etAnio)
    }

    private fun initListener() {

        binding.etAnio.setOnClickListener {
            NumberPickerDialog.newInstance(binding.etAnio, "Año").show(childFragmentManager, null)
        }

        binding.etAnio.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isEmpty()) return

                viewModel.reporteTicketMensual(s.toString().trim())
            }
        })
    }

    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            UtilsMessage.showAlertOk(
                                "Error", it.message, requireContext()
                            )
                            viewModel.resetUiState()
                        }

                        UiState.Loading -> binding.progressBar.isVisible = true
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            generarGrafico(it.data)
                        }

                        null -> Unit
                    }
                }

            }
        }
    }

    private fun generarGrafico(reporte: List<ReporteTicketMensual>) {

        entries.clear()

        val nombreMeses = reporte.mapIndexed { index, reporteTicketMensual ->
            entries.add(BarEntry(index.toFloat(), reporteTicketMensual.total.toFloat()))
            reporteTicketMensual.mes
        }

        val total = reporte.sumOf { it.total }

        binding.barChart.apply {
            description.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return nombreMeses[value.toInt()]
                    }
                }
            }

            val dataSet = BarDataSet(
                entries,
                "Reporte del año ${binding.etAnio.text} TOTAL: ${
                    UtilsCommon.formatFromDoubleToString(
                        total
                    )
                }"
            ).apply {
                setColors(*ColorTemplate.MATERIAL_COLORS)

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return UtilsCommon.formatFromDoubleToString(value.toDouble())
                    }
                }
            }

            data = BarData(dataSet).apply {
                setValueTextSize(10f)
                barWidth = 0.9f
            }

            animateXY(500, 1000)
            invalidate()
        }

    }

}