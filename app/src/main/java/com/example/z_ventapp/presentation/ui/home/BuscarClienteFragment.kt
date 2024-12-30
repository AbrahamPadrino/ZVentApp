package com.example.z_ventapp.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentBuscarClienteBinding
import com.example.z_ventapp.domain.model.Cliente
import com.example.z_ventapp.presentation.adapter.BuscarClienteAdapter
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class BuscarClienteFragment : DialogFragment(), BuscarClienteAdapter.IOnClickListener {

    private lateinit var binding: FragmentBuscarClienteBinding
    private val viewModel: BuscarClienteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBuscarClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.listarCliente("")
    }

    private fun initListener() {
        binding.includeToolbar.toolbar.title = getString(R.string.buscar_cliente)
        binding.includeToolbar.toolbar.subtitle = ""
        binding.includeToolbar.toolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.rvLista.apply {
            adapter = BuscarClienteAdapter(this@BuscarClienteFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.includeBuscar.tilBuscar.setEndIconOnClickListener {
            binding.includeBuscar.etBuscar.setText("")
            UtilsCommon.hideKeyboard(requireContext(), it)
        }

        binding.includeBuscar.etBuscar.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.listarCliente(s.toString().trim())
            }
        })
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiStateCliente.collect {
                    when (it) {
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            UtilsMessage.showAlertOk(
                                "ERROR", it.message, requireContext()
                            )
                            viewModel.resetUiStateCliente()
                        }

                        UiState.Loading -> binding.progressBar.isVisible = true
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            (binding.rvLista.adapter as BuscarClienteAdapter).submitList(it.data)
                        }

                        null -> Unit
                    }
                }

            }
        }
    }

    override fun clickCliente(model: Cliente) {
        UtilsCommon.hideKeyboard(requireContext(), binding.root.rootView)
        viewModel.asignarCliente(model)
        dismiss()
    }
}