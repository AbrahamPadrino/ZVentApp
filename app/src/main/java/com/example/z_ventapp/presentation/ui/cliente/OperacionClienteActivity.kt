package com.example.z_ventapp.presentation.ui.cliente

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ActivityOperacionClienteBinding
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class OperacionClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionClienteBinding
    private val viewModel: OperacionClienteViewModel by viewModels()
    private val args: OperacionClienteActivityArgs by navArgs()
    private var _id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()

        _id = args.id
        if (args.id > 0)
            viewModel.obtenerCliente(args.id)
    }

    private fun initListener() {
        binding.includeEditar.toolbar.title = if (args.id > 0)
            getString(R.string.editar_cliente)
        else getString(R.string.nuevo_cliente)

        binding.includeEditar.toolbar.subtitle = ""

        binding.includeEditar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)

            if (binding.etNombre.text.toString().trim().isEmpty() ||
                binding.etNrodoc.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showToast(this, "Todos los campos son obligatorios")
                return@setOnClickListener
            }

            viewModel.grabar(
                Cliente().apply {
                    id = _id
                    nombre = binding.etNombre.text.toString().trim()
                    nrodoc = binding.etNrodoc.text.toString().trim()
                    vigente = if (binding.swVigente.isChecked) 1 else 0
                }
            )
        }
    }

    private fun initObserver() {
        viewModel.uiStateObtener.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, this
                    )
                    viewModel.resetUiStateObtener()
                }

                UiState.Loading -> binding.progressBar.isVisible = true
                is UiState.Success -> {
                    binding.progressBar.isVisible = false
                    if (it.data == null) return@observe

                    binding.etNombre.setText(it.data.nombre)
                    binding.etNrodoc.setText(it.data.nrodoc)
                    binding.swVigente.isChecked = it.data.vigente == 1
                }

                null -> Unit
            }
        }


        viewModel.uiStateGrabar.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, this
                    )
                    viewModel.resetUiStateGrabar()
                }

                UiState.Loading -> binding.progressBar.isVisible = true
                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data == 0) {
                        UtilsMessage.showAlertOk("ERROR", "Error al grabar", this)
                        return@observe
                    }

                    UtilsMessage.showToast(
                        this, "REGISTRO GUARDADO"
                    )
                    UtilsCommon.cleanEditText(binding.root.rootView)
                    _id = 0
                    binding.etNombre.requestFocus()
                    viewModel.resetUiStateGrabar()
                }

                null -> Unit
            }
        }
    }
}