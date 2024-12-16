package com.example.z_ventapp.presentation.ui.producto

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.databinding.FragmentProductoBinding
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.presentation.adapter.ProductoAdapter
import com.example.z_ventapp.presentation.common.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class ProductoFragment : Fragment(), ProductoAdapter.IOnClickListener {

    private lateinit var binding: FragmentProductoBinding
    private val viewModel: ProductoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.buscarPorDato("")
    }

    private fun initListener() {

        binding.rvLista.apply {
            adapter = ProductoAdapter(this@ProductoFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.includeBuscar.tilBuscar.setEndIconOnClickListener {
            binding.includeBuscar.etBuscar.setText("")
            UtilsCommon.hideKeyboard(requireContext(), it)
        }

        binding.includeBuscar.etBuscar.addTextChangedListener(object : SimpleTextWatcher() {

            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                viewModel.buscarPorDato(s.toString().trim())
            }
        })

        binding.fabNuevo.setOnClickListener {
            findNavController().navigate(
                ProductoFragmentDirections.actionNavProductoToOperacionProductoActivity(0)
            )
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Colectar Para Listar
                launch {
                    viewModel.uiStateListar.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ERROR", it.message, requireContext()
                                )
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                (binding.rvLista.adapter as ProductoAdapter).submitList(it.data)
                            }
                        }
                    }
                }
                // Colectar Para Eliminar
                launch {
                    viewModel.uiStateEliminar.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ERROR", it.message, requireContext()
                                )
                                viewModel.resetUiStateEliminar()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showToast(
                                    requireContext(), "Registro eliminado"
                                )
                                viewModel.resetUiStateEliminar()
                            }

                            null -> Unit
                        }
                    }
                }
            }
        }
    }

    // Funciones miembros de IOnClickListener
    override fun clickEditar(model: Producto) {
        UtilsCommon.hideKeyboard(requireContext(), requireView())

        findNavController().navigate(
            ProductoFragmentDirections.actionNavProductoToOperacionProductoActivity(model.id)
        )
    }

    override fun clickEliminar(model: Producto) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Eliminar")
            setMessage("¿Desea eliminar el registro: ${model.descripcion}?")
            setCancelable(false)

            setPositiveButton("SI") { dialog, _ ->
                viewModel.eliminar(model)
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }

        }.create().show()
    }
    //
}