package com.example.proyecto_german

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        setupButton()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupButton() {
        binding.botonGuardar.setOnClickListener {
            sendDataToServer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding= null
    }
    private fun sendDataToServer(){
        val dataStr = "Profundidad:${binding.profundidad.text.toString().toInt()}"
        Log.i("Nombre",dataStr)
    }
}