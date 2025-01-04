package com.example.z_ventapp.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentCatalogoBinding
import com.example.z_ventapp.domain.model.Producto
import com.example.z_ventapp.presentation.adapter.CatalogoAdapter
import com.example.z_ventapp.presentation.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.SimpleTextWatcher
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class CatalogoFragment : DialogFragment(), CatalogoAdapter.IOnClickListener {

    private lateinit var binding: FragmentCatalogoBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCatalogoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.listarProducto("")
    }

    private fun initListener() {

        binding.includeToolbar.toolbar.title = getString(R.string.catalogo_producto)
        binding.includeToolbar.toolbar.subtitle = ""
        binding.includeToolbar.toolbar.setSubtitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.accent
            )
        )

        binding.includeBuscar.tilBuscar.startIconDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.baseline_barcode_reader_24)

        binding.includeToolbar.toolbar.setNavigationOnClickListener { dismiss() }

        binding.rvLista.apply {
            adapter = CatalogoAdapter(this@CatalogoFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.includeBuscar.tilBuscar.setEndIconOnClickListener {
            binding.includeBuscar.etBuscar.setText("")
            UtilsCommon.hideKeyboard(requireContext(), it)
        }

        binding.includeBuscar.etBuscar.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.listarProducto(s.toString().trim())
            }
        })

    }

    private fun initObserver() {

        viewModel.totalItem.observe(viewLifecycleOwner) {
            binding.includeToolbar.toolbar.subtitle = if(it > 0) "Items: ${it.toString()}" else ""
        }

        // observar UiState Listar
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiStateListarProducto.collect {
                    when (it) {
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            UtilsMessage.showAlertOk(
                                "ERROR", it.message, requireContext()
                            )
                            viewModel.resetUiStateListarProducto()
                        }

                        UiState.Loading -> binding.progressBar.isVisible = true

                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            (binding.rvLista.adapter as CatalogoAdapter).submitList(it.data)
                        }

                        null -> Unit
                    }
                }
            }
        }

    }

    override fun clickAgregar(model: Producto) {
        TODO("Not yet implemented")
    }

}