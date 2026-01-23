package com.example.proyecto_german

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionProfundidadBinding

class FormFragment: Fragment() {
    private var _biding :FragmentFormularioPerforacionBinding? =null
    private val binding get() = _biding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioPerforacionBinding.inflate(inflater,container,false)
        accionBoton()
        return binding.root
    }
    fun accionBoton(){
        print("LLegue aca 1")
        val formFragment = FormFragmentProfundidad()
        val miBoton = binding.root.findViewById<Button>(R.id.boton_continuar)
        miBoton.setOnClickListener {
//
            findNavController().navigate(R.id.action_formFragment_to_formFragmentProfundidad)
        }
    }
}