package com.example.z_ventapp.presentation.ui.crearcuenta

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.z_ventapp.databinding.ActivityCrearCuentaBinding
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.libpcs.UtilsSecurity

@AndroidEntryPoint
class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearCuentaBinding
    private val viewModel: CrearCuentaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()

    }

    private fun initListener() {

        binding.includeEditar.toolbar.title = "Crear Cuenta"
        binding.includeEditar.toolbar.subtitle = "Ingrese los datos de la cuenta"

        binding.includeEditar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)

            if (binding.etClave.text.toString().trim().isEmpty() ||
                binding.etNombre.text.toString().trim().isEmpty() ||
                binding.etEmail.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk("ADVERTENCIA", "Todos los campos son obligatorios", this)
                return@setOnClickListener
            }

            viewModel.grabarCuenta(
                Usuario().apply {
                    id = 0
                    nombre = binding.etNombre.text.toString().trim()
                    email = binding.etEmail.text.toString().trim()
                    clave = UtilsSecurity.createHashSha512(binding.etClave.text.toString().trim())
                    vigente = 1
                }
            )
        }

    }

    //Observer de la vista para actualizar de UI
    private fun initObserver() {
        viewModel.uiStateGrabar.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk("ERROR", it.message, this)
                    viewModel.resetUiStateGrabar()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if (it.data == null) return@observe

                    MainActivity.mUsuario = it.data
                    UtilsMessage.showToast(this, "Cuenta creada")

                    startActivity(
                        Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                    finish()
                }

                null -> Unit
            }
        }

    }
}