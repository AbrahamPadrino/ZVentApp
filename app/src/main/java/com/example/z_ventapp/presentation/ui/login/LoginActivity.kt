package com.example.z_ventapp.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.z_ventapp.databinding.ActivityLoginBinding
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.ui.crearcuenta.CrearCuentaActivity
import com.example.z_ventapp.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage
import pe.pcs.libpcs.UtilsSecurity

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        initObserver()
    }

    // Verificar si existe la cuenta
    private fun initObserver() {
        binding.tvCrearCuenta.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)
            viewModel.existeCuenta()
        }
        // Acceder
        binding.btAcceder.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)

            if (binding.etEmail.text.toString().trim().isEmpty() ||
                binding.etClave.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showAlertOk("ADVERTENCIA", "Todos los campos son obligatorios", this)
                return@setOnClickListener
            }

            viewModel.login(
                binding.etEmail.text.toString().trim(),
                UtilsSecurity.createHashSha512(binding.etClave.text.toString().trim())
            )
        }
    }

    // Observa los cambios de la vista
    private fun initListener() {
        // Verificar si existe la cuenta
        viewModel.uiStateExisteCuenta.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk("ERROR", it.message, this)
                    viewModel.resetUiStateExisteCuenta()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if(it.data > 0) {
                        UtilsMessage.showAlertOk("ERROR", "Ya existe una cuenta", this)
                        return@observe
                    }
                    startActivity(Intent(this, CrearCuentaActivity::class.java))
                }
                null -> Unit
            }
        }

        // Ejecutar Login
        viewModel.uiStateLogin.observe(this) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk("ERROR", it.message, this)
                    viewModel.resetUiStateLogin()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if(it.data == null) {
                        UtilsMessage.showAlertOk(
                            "ADVERTENCIA",
                            "El email y/o la clave son incorrectas", this
                        )
                        return@observe
                    }
                    // Guarda los datos en el objeto usuario
                    MainActivity.mUsuario = it.data

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                null -> Unit
            }
        }
    }
}