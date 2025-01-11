package com.example.z_ventapp.presentation.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.example.z_ventapp.databinding.FragmentHomeBinding
import com.example.z_ventapp.domain.model.DetalleTicket
import com.example.z_ventapp.domain.model.Ticket
import com.example.z_ventapp.presentation.adapter.CarritoAdapter
import com.example.z_ventapp.presentation.common.UiState
import com.example.z_ventapp.presentation.common.UtilsAnimation.crearTransformacion
import com.example.z_ventapp.presentation.ui.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon
import pe.pcs.libpcs.UtilsDate
import pe.pcs.libpcs.UtilsMessage

@AndroidEntryPoint
class HomeFragment : Fragment(), CarritoAdapter.IOnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModelCliente: BuscarClienteViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initObserver()
    }

    private fun initListener() {
        // Preparar el RecyclerView
        binding.rvLista.apply {
            adapter = CarritoAdapter(this@HomeFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }

        //
        binding.etCliente.setOnClickListener {
            val dialog = BuscarClienteFragment()
            dialog.show(childFragmentManager, "BuscarClienteFragment")
        }
        //
        binding.fabOpciones.setOnClickListener {

            UtilsCommon.hideKeyboard(requireContext(), it)

            TransitionManager.beginDelayedTransition(
                binding.coordinatorLayout,
                crearTransformacion(
                    binding.fabOpciones,
                    binding.mcOpciones
                )
            )

            binding.mcOpciones.visibility = View.VISIBLE
            binding.fabOpciones.visibility = View.GONE
        }
        //
        binding.ibCancelar.setOnClickListener {
            minimizarOpcion()
        }
        //
        binding.btAgregarProducto.setOnClickListener {
            minimizarOpcion()
            CatalogoFragment().show(childFragmentManager, "CatalogoFragment")
        }
        //
        binding.btConfirmarTicket.setOnClickListener {
            minimizarOpcion()

            if(homeViewModel.listaCarrito.value.isNullOrEmpty())
                return@setOnClickListener

            if(MainActivity.mUsuario == null){
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Debe Iniciar Sesión",
                    requireContext()
                )
                return@setOnClickListener
            }
            if(viewModelCliente.itemCliente.value == null){
                UtilsMessage.showAlertOk(
                    "ADVERTENCIA", "Debe Seleccionar un Cliente",
                    requireContext()
                )
                return@setOnClickListener
            }

            homeViewModel.grabarTicket(
                Ticket().apply {
                    idusuario = MainActivity.mUsuario!!.id
                    fecha = UtilsDate.obtenerFechaHoraActual()
                    total = homeViewModel.totalImporte.value!!
                    estado = "Vigente"
                    cliente = viewModelCliente.itemCliente.value
                    detalles = homeViewModel.listaCarrito.value!!
                }
            )
        }
    }

    private fun initObserver() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModelCliente.itemCliente.collect{
                        binding.etCliente.setText(it?.nombre)
                    }
                }
            }
        }
         // Mensaje de exito o fracaso de Grabar Ticket
        homeViewModel.listaCarrito.observe(viewLifecycleOwner) {
            (binding.rvLista.adapter as CarritoAdapter).setData(it)
            binding.tvMensaje.isVisible = it.size == 0
        }
        // Observa el Importe
        homeViewModel.totalImporte.observe(viewLifecycleOwner) {
            binding.tvImporte.text = UtilsCommon.formatFromDoubleToString(it)
        }
        // Observa el UiState Grabar Ticket
        homeViewModel.uiStateGrabarTicket.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Error -> {
                    binding.progressBar.isVisible = false
                    UtilsMessage.showAlertOk(
                        "ERROR", it.message, requireContext()
                    )
                    homeViewModel.resetUiStateGrabarTicket()
                }

                UiState.Loading -> binding.progressBar.isVisible = true

                is UiState.Success -> {
                    binding.progressBar.isVisible = false

                    if(it.data < 1) return@observe

                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("Información")
                        setMessage("Ticket Grabado con Exito")
                        setCancelable(false)

                        setPositiveButton("Aceptar"){
                            dialog, _ ->
                            limpiarDatos()
                            dialog.dismiss()
                        }
                    }.create().show()
                }
                null -> Unit
            }
        }
    }



    private fun minimizarOpcion(){
        TransitionManager.beginDelayedTransition(
            binding.coordinatorLayout,
            crearTransformacion(
                binding.mcOpciones,
                binding.fabOpciones
            )
        )

        binding.mcOpciones.visibility = View.GONE
        binding.fabOpciones.visibility = View.VISIBLE
    }

    override fun clickMas(model: DetalleTicket) {
        homeViewModel.aumentarCantidadProducto(model)
    }

    override fun clickMenos(model: DetalleTicket) {
        homeViewModel.disminuirCantidadProducto(model)
    }

    override fun clickEliminar(model: DetalleTicket) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Eliminar producto")
            setMessage("¿Desea eliminar el producto: ${model.descripcion}?")
            setCancelable(false)

            setPositiveButton("SI") { dialog, _ ->
                homeViewModel.quitarProductoCarrito(model)

                if(homeViewModel.listaCarrito.value?.size == 0)
                    viewModelCliente.asignarCliente(null)

                dialog.dismiss()
            }

            setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun limpiarDatos() {
        homeViewModel.limpiarCarrito()
        viewModelCliente.asignarCliente(null)
        homeViewModel.resetUiStateGrabarTicket()
    }

}