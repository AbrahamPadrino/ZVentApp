package com.example.z_ventapp.presentation.ui.usuario

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ActivityOperacionUsuarioBinding
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.libpcs.UtilsSecurity

/** Grabar nuevo registro o editar los datos de un registro existente*/

@AndroidEntryPoint
class OperacionUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionUsuarioBinding
    private val viewModel: OperacionUsuarioViewModel by viewModels()
    private val args: OperacionUsuarioActivityArgs by navArgs() // Recibir argumentos
    private var _id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()

        _id = args.id
        if(args.id > 0)
            viewModel.obtenerPorId(args.id)

    }

    private fun initListener() {

        binding.includeEditar.toolbar.title =
            if(args.id > 0) getString(R.string.editar_usuario) else getString(R.string.nuevo_usuario)
        binding.includeEditar.toolbar.subtitle = ""

        binding.includeEditar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)

            if (binding.etClave.text.toString().trim().isEmpty() ||
                binding.etNombre.text.toString().trim().isEmpty() ||
                binding.etEmail.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA",
                    "Todos los campos son obligatorios",
                    this
                )
                return@setOnClickListener
            }
            // Grabar
            viewModel.grabar(
                Usuario().apply {
                    id = _id
                    clave = if (args.id ==0 ) UtilsSecurity.createHashSha512(
                        binding.etClave.text.toString().trim()
                    ) else binding.etClave.text.toString().trim()
                    nombre = binding.etNombre.text.toString().trim()
                    email = binding.etEmail.text.toString().trim()
                    vigente = if (binding.swVigente.isChecked) 1 else 0
                }
            )
        }
    }

    private fun initObserver() {
        // observar a los dos modificadores de estado
        viewModel.uiStateObtenerPorId.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR",
                        it.message,
                        this
                    )
                    viewModel.resetUiStateObtenerPorId()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {

                    binding.progressBar.isVisible = false

                    if (it.data == null) return@observe

                    binding.etClave.setText(it.data.clave)
                    binding.etNombre.setText(it.data.nombre)
                    binding.etEmail.setText(it.data.email)
                    binding.swVigente.isChecked = it.data.vigente == 1
                    binding.tilClave.isVisible = false
                }

                null -> Unit
            }
        }

        viewModel.uiStateGrabar.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false

                    UtilsMessage.showAlertOk(
                        "ERROR",
                        it.message,
                        this)
                    viewModel.resetUiStateGrabar()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data == 0) {
                        UtilsMessage.showAlertOk(
                            "ERROR",
                            "No se pudo grabar el registro",
                            this
                        )
                        return@observe
                    }

                    UtilsMessage.showToast(this, "Grabado correctamente")
                    binding.tilClave.isVisible = true
                    UtilsCommon.cleanEditText(binding.root.rootView)
                    binding.etNombre.requestFocus()
                    viewModel.resetUiStateGrabar()
                    _id = 0
                }

                null -> Unit
            }
        }
    }
}