package com.example.z_ventapp.presentation.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.transition.TransitionManager
import com.example.z_ventapp.databinding.FragmentHomeBinding
import com.example.z_ventapp.presentation.common.UtilsAnimation.crearTransformacion
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pe.pcs.libpcs.UtilsCommon

@AndroidEntryPoint
class HomeFragment : Fragment() {

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

}