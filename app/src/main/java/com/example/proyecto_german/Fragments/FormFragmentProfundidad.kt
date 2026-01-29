package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.R
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionProfundidadBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FormFragmentProfundidad: Fragment() {
    private var _biding : FragmentFormularioPerforacionProfundidadBinding? =null
    private val binding get() = _biding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioPerforacionProfundidadBinding.inflate(inflater,container,false)
        binding.root.findViewById<FloatingActionButton>(R.id.button_floting_add).setOnClickListener {
            findNavController().navigate(R.id.action_formFragmentProfundidad_to_formFragmentSTP2)
        }
        return binding.root
    }

}