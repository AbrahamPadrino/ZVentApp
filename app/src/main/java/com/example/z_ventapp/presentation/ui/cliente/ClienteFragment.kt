package com.example.z_ventapp.presentation.ui.cliente

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.z_ventapp.databinding.FragmentClienteBinding
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.presentation.adapter.ClienteAdapter
import com.example.z_ventapp.presentation.common.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage


@AndroidEntryPoint
class ClienteFragment : Fragment(), ClienteAdapter.IOnClickListener {

    private lateinit var binding: FragmentClienteBinding
    private val viewModel: ClienteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.listar("")
    }

    private fun initListener() {

        binding.rvLista.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ClienteAdapter(this@ClienteFragment)
        }

        binding.includeBuscar.tilBuscar.setEndIconOnClickListener {
            binding.includeBuscar.etBuscar.setText("")
            UtilsCommon.hideKeyboard(requireContext(), it)
        }

        binding.includeBuscar.etBuscar.addTextChangedListener(object : TextWatcher  {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.listar(binding.includeBuscar.etBuscar.text.toString().trim())
            }
        })

        binding.fabNuevo.setOnClickListener {
            findNavController().navigate(
                ClienteFragmentDirections.actionNavClienteToOperacionClienteActivity(0)
            )
        }
    }


    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiStateListar.collect {
                        when (it) {
                            is UiState.Error -> {
                                binding.progressBar.isVisible = false
                                UtilsMessage.showAlertOk(
                                    "ERROR", it.message, requireContext()
                                )
                                viewModel.resetUiStateListar()
                            }

                            UiState.Loading -> binding.progressBar.isVisible = true
                            is UiState.Success -> {
                                binding.progressBar.isVisible = false
                                (binding.rvLista.adapter as ClienteAdapter).submitList(it.data)
                            }

                            null -> Unit
                        }
                    }
                }

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
                                    requireContext(), "REGISTRO ELIMINADO"
                                )
                                viewModel.listar(
                                    binding.includeBuscar.etBuscar.text.toString().trim()
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

    override fun clickEditar(model: Cliente) {
        UtilsCommon.hideKeyboard(requireContext(), requireView())

        findNavController().navigate(
            ClienteFragmentDirections.actionNavClienteToOperacionClienteActivity(model.id)
        )
    }

    override fun clickEliminar(model: Cliente) {
        UtilsCommon.hideKeyboard(requireContext(), requireView())

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${model.nombre}?")
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
}