package com.example.proyecto_german

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import com.example.proyecto_german.databinding.FragmentFormularioSptBinding

class FormFragmentSTP: Fragment(){
    private var _biding : FragmentFormularioSptBinding? =null
    private val binding get() = _biding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioSptBinding.inflate(inflater,container,false)
        return binding.root
    }
}