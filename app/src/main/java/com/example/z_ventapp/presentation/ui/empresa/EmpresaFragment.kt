package com.example.z_ventapp.presentation.ui.empresa

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
import com.example.z_ventapp.databinding.FragmentEmpresaBinding
import com.example.z_ventapp.domain.model.Empresa
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class EmpresaFragment : Fragment() {

    private lateinit var binding: FragmentEmpresaBinding
    private val viewModel: EmpresaViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmpresaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.obtenerEmpresa()
    }

    private fun initListener() {

        binding.fabGrabar.setOnClickListener {
            if (binding.etRazonSocial.text.toString().isEmpty() ||
                binding.etRuc.text.toString().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Debe ingresar un ruc y razon social", requireContext()
                )
                return@setOnClickListener
            }

            viewModel.grabar(
                Empresa().apply {
                    razonSocial = binding.etRazonSocial.text.toString()
                    ruc = binding.etRuc.text.toString()
                    direccion = binding.etDireccion.text.toString()
                    telefono = binding.etTelefono.text.toString()
                }
            )
        }
    }

    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiStateObtener.collect {
                        when(it){
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ADVERTENCIA", it.message, requireContext()
                                )
                                viewModel.resetUiStateObtener()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true

                            is UiState.Success -> {
                                binding.progressBar.isVisible = false

                                binding.etRazonSocial.setText(it.data?.razonSocial)
                                binding.etRuc.setText(it.data?.ruc)
                                binding.etDireccion.setText(it.data?.direccion)
                                binding.etTelefono.setText(it.data?.telefono)
                            }

                            null -> Unit
                        }
                    }

                }

                launch {
                    viewModel.uiStateGrabar.collect {
                        when(it){
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