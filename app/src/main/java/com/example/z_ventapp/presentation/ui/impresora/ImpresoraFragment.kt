package com.example.z_ventapp.presentation.ui.impresora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.z_ventapp.databinding.FragmentImpresoraBinding
import com.example.z_ventapp.domain.model.Impresora
import com.example.z_ventapp.presentation.common.ConstantsApp
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ImpresoraFragment : Fragment() {

    private lateinit var binding: FragmentImpresoraBinding
    private val viewModel: ImpresoraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImpresoraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.obtener()
    }

    private fun initListener(){
        binding.fabGrabar.setOnClickListener {
            if (binding.rbQuickPrinter.isChecked &&
                binding.etAlias.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "Atención", "Debe ingresar el alias de la impresora", requireContext()
                )
                return@setOnClickListener
            }

            val tipoImpresora = if (binding.rbQuickPrinter.isChecked) ConstantsApp.DRIVER_QUICK_PRINTER else ConstantsApp.DRIVER_RAW_BT

            viewModel.grabar(
                Impresora().apply {
                    alias = binding.etAlias.text.toString()
                    tipo = tipoImpresora
                }
            )
        }

    }

    private fun initObserver(){

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Cargar los Registros
                launch {
                    viewModel.uiStateObtener.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ERROR", it.message, requireContext()
                                )
                                viewModel.resetUiStateObtener()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false

                                it.data?.let { impresora ->
                                    binding.etAlias.setText(impresora.alias)

                                    if (it.data.tipo.lowercase() == "quick printer")
                                        binding.rbQuickPrinter.isChecked = true
                                    else
                                        binding.rbRawBt.isChecked = true
                                }
                                viewModel.resetUiStateObtener()
                            }

                            null -> Unit
                        }
                    }
                }

                // Grabar los Registros
                launch {
                    viewModel.uiStateGrabar.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ERROR", it.message, requireContext()
                                )
                                viewModel.resetUiStateGrabar()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showToast(
                                    requireContext(), "Datos grabados"
                                )
                                viewModel.resetUiStateGrabar()
                                UtilsCommon.hideKeyboard(requireContext(), requireView())
                            }

                            null -> Unit
                        }
                    }
                }
            }
        }

    }

}