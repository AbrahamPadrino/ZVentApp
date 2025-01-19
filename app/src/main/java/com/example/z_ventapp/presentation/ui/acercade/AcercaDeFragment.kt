package com.example.z_ventapp.presentation.ui.acercade

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.FragmentAcercaDeBinding

class AcercaDeFragment : Fragment() {

    private lateinit var binding: FragmentAcercaDeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAcercaDeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Cambiar icono programaticamente.
        binding.includeEditar.imageView2.setImageResource(R.drawable.icono_shopping_fondo)
    }
}