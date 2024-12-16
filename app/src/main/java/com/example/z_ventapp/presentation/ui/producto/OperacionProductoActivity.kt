package com.example.z_ventapp.presentation.ui.producto

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ActivityOperacionProductoBinding
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.presentation.common.UiState
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import pe.pcs.libpcs.PermissionUtils
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class OperacionProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperacionProductoBinding
    private val viewModel: OperacionProductoViewModel by viewModels()
    private val args: OperacionProductoActivityArgs by navArgs()
    private var _id = 0

    // libreria de permisos
    private lateinit var permisos: PermissionUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperacionProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // permisos
        permisos = PermissionUtils(this)

        initListener()
        initObserver()

        _id = args.id
        if (_id > 0)
            viewModel.obtener(_id)
    }

    private fun initListener() {
        binding.includeEditar.toolbar.title =
            if (args.id > 0) getString(R.string.editar_producto) else getString(R.string.nuevo_producto)
        binding.includeEditar.toolbar.subtitle = ""

        binding.includeEditar.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tilCodigoBarra.setStartIconOnClickListener {
            UtilsCommon.hideKeyboard(this, it)
            leerCodigoBarra()

        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.hideKeyboard(this, it)

            if (binding.etCodigoBarra.text.toString().trim().isEmpty() ||
                binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty()
            ) {
                UtilsMessage.showToast(this, "Debe ingresar todos los datos")
                return@setOnClickListener
            }

            viewModel.grabar(
                Producto().apply {
                    id = _id
                    codigobarra = binding.etCodigoBarra.text.toString().trim()
                    descripcion = binding.etDescripcion.text.toString().trim()
                    precio = binding.etPrecio.text.toString().trim().toDouble()
                }
            )
        }
    }

    private fun initObserver() {
        //  Observa Estado De Listar
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

                    binding.etCodigoBarra.setText(it.data.codigobarra)
                    binding.etDescripcion.setText(it.data.descripcion)
                    binding.etPrecio.setText(UtilsCommon.formatFromDoubleToString(it.data.precio))
                }

                null -> Unit
            }
        }

        //  Observa Estado De Grabar
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

                    if (it.data == 0) return@observe

                    UtilsMessage.showToast(this, "Registro grabado")
                    UtilsCommon.cleanEditText(binding.root.rootView)
                    binding.etDescripcion.requestFocus()
                    viewModel.resetUiStateGrabar()
                    _id = 0
                }

                null -> Unit
            }
        }

    }

    // Trabajando con la camara para leer el codigo de barra
    private val barcodeLauncher = registerForActivityResult(ScanContract()) {
        if (it.contents != null)
            binding.etCodigoBarra.setText(it.contents)

    }

    private fun leerCodigoBarra() {
        permisos.requestPermissions(arrayOf(android.Manifest.permission.CAMERA)) { result ->
            val todosLosPermisosConcedidos = result.values.all { it }
            if (todosLosPermisosConcedidos) {
                val opciones = ScanOptions()
                opciones.setOrientationLocked(false)
                barcodeLauncher.launch(opciones)
            } else {
                UtilsMessage.showToast(this, "Debe aceptar los permisos de la camara")
            }
        }
    }
}