package com.example.z_ventapp.presentation.ui.usuario

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
import com.example.z_ventapp.databinding.FragmentUsuarioBinding
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.presentation.adapter.UsuarioAdapter
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsMessage

// Muestra el listado de usuarios

@AndroidEntryPoint
class UsuarioFragment : Fragment(), UsuarioAdapter.IOnclickListener {

    private lateinit var binding: FragmentUsuarioBinding
    private val viewModel: UsuarioViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()

        viewModel.listar("")    // Listar todos

    }

    private fun initListener() {
        // Preparar el recycler
        binding.rvLista.apply {
            adapter = UsuarioAdapter(this@UsuarioFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }
        // Limpia el campo de buscar cada vez que presione el icono x
        binding.includeBuscar.tilBuscar.setEndIconOnClickListener {
            binding.includeBuscar.etBuscar.setText("")
            UtilsCommon.hideKeyboard(requireContext(), it)
        }
        // Buscar al presionar una tecla
        binding.includeBuscar.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.listar(s.toString().trim())
            }

        })
        // Nuevo
        binding.fabNuevo.setOnClickListener {
            findNavController().navigate(
                UsuarioFragmentDirections.actionNavUsuarioToOperacionUsuarioActivity(0)
            )
        }

    }

    private fun initObserver() {
        // Observa UiState Listar
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // Ejecutar cuando esté en STARTED
                viewModel.uiStateListar.collect {   // Colectar el estado de la UI
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
                            (binding.rvLista.adapter as UsuarioAdapter).submitList(it.data) // Pasar el listado al Recycler a traves del adapter
                        }

                        null -> Unit
                    }
                }
            }
        }

        // Observa UiState Eliminar
        viewModel.uiStateEliminar.observe(viewLifecycleOwner) {
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
                    UtilsMessage.showToast(requireContext(), "Usuario eliminado")
                    viewModel.resetUiStateEliminar()
                }

                null -> Unit
            }
        }
    }

    override fun clickEditar(usuario: Usuario) {
        if (MainActivity.mUsuario == usuario) {
            UtilsMessage.showToast(requireContext(), "No puede editar su propio usuario")
            return
        }

        findNavController().navigate(
            UsuarioFragmentDirections.actionNavUsuarioToOperacionUsuarioActivity(usuario.id)
        )
    }

    override fun clickEliminar(usuario: Usuario) {
        UtilsCommon.hideKeyboard(requireContext(), requireView())

        if (MainActivity.mUsuario == usuario) {
            UtilsMessage.showToast(requireContext(), "No se puede eliminar su propio usuario")
            return
        }

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${usuario.nombre}?")
            setCancelable(false)

            setPositiveButton("SI") { dialog, _ ->
                viewModel.eliminar(usuario)
                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }
}