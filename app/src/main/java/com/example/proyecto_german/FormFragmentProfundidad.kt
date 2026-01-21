package com.example.proyecto_german

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionProfundidadBinding
import com.example.proyecto_german.databinding.FragmentHomeBinding

class FormFragmentProfundidad: Fragment() {
    private var _biding : FragmentFormularioPerforacionProfundidadBinding? =null
    private val binding get() = _biding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioPerforacionProfundidadBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}