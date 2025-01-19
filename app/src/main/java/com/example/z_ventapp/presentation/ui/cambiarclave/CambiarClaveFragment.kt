package com.example.z_ventapp.presentation.ui.cambiarclave

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentCambiarClaveBinding
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.ui.login.LoginActivity
import com.example.z_ventapp.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.libpcs.UtilsSecurity

@AndroidEntryPoint
class CambiarClaveFragment : Fragment() {

    private lateinit var binding: FragmentCambiarClaveBinding
    private val viewModel: CambiarClaveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()
    }

    private fun initListener() {
        binding.fabGrabar.setOnClickListener {
            UtilsCommon.hideKeyboard(requireActivity(), it)

            // Validar campos
            if(binding.etClaveActual.text.toString().trim().isEmpty() ||
                binding.etNuevaClave.text.toString().trim().isEmpty()) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Todos los campos son obligatorios", requireContext()
                )
                return@setOnClickListener
            }

            // Validar Clave Actual
            if (UtilsSecurity.createHashSha512(
                    binding.etClaveActual.text.toString().trim()
                ) != MainActivity.mUsuario?.clave
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Clave Actual Incorrecta", requireContext()
                )
                return@setOnClickListener
            }

            // Grabar Clave
            viewModel.actualizarClave(
                MainActivity.mUsuario!!.id,
                UtilsSecurity.createHashSha512(binding.etNuevaClave.text.toString().trim())
            )
        }
    }

    private fun initObserver() {
        viewModel.uiState.observe(viewLifecycleOwner){
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, requireContext()
                    )
                    viewModel.resetUiState()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "EXITO", "Clave Actualizada", requireContext()
                    )
                    viewModel.resetUiState()
                    UtilsCommon.cleanEditText(requireView())

                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }

                null -> Unit
            }
        }
    }

}