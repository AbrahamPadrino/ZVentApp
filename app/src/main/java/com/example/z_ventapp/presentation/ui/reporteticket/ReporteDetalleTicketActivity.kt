package com.example.z_ventapp.presentation.ui.reporteticket

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ActivityReporteDetalleTicketBinding
import com.example.z_ventapp.presentation.adapter.DetalleTicketAdapter
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ReporteDetalleTicketActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityReporteDetalleTicketBinding
    private val viewModel: ReporteDetalleTicketViewModel by viewModels()
    private val args: ReporteDetalleTicketActivityArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReporteDetalleTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()

        if (args.id == 0) return

        viewModel.reporteDetalleTicket(args.id)
        viewModel.obtenerTicketPorId(args.id)

    }

    private fun initListener() {

        binding.includeToolbar.toolbar.title = getString(R.string.detalle_ticket)
        binding.includeToolbar.toolbar.subtitle = ""

        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvLista.apply {
            adapter = DetalleTicketAdapter()
            layoutManager = LinearLayoutManager(this@ReporteDetalleTicketActivity)
        }
    }

    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiStateObtener.collect {
                        when (it) {
                            is UiState.Error -> {
                                UtilsMessage.showAlertOk(
                                    "Error", it.message, this@ReporteDetalleTicketActivity
                                )
                                viewModel.resetUiStateObtener()
                            }

                            UiState.Loading -> Unit
                            is UiState.Success -> {
                                binding.tvCliente.text = it.data?.cliente
                                binding.tvFecha.text = it.data?.fecha
                                binding.tvTitulo.text = "TICKET #${it.data?.id.toString()}"
                                binding.tvEstado.text = it.data?.estado
                                binding.tvTotal.text =
                                    UtilsCommon.formatFromDoubleToString(it.data?.total ?: 0.0)

                                viewModel.resetUiStateObtener()
                            }

                            null -> Unit
                        }
                    }
                }

                launch {
                    viewModel.uiStateDetalle.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "Error", it.message, this@ReporteDetalleTicketActivity
                                )
                                viewModel.resetUiStateDetalle()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                (binding.rvLista.adapter as DetalleTicketAdapter).submitList(it.data)
                                viewModel.resetUiStateDetalle()
                            }

                            null -> Unit
                        }
                    }
                }
            }
        }

    }
}