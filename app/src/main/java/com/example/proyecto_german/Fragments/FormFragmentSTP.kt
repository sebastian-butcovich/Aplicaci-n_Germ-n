package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Sucs
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
        if(chequedoDeDatos()) {
            val dataProfundidad = binding.profundidad.text.toString().toDouble()
            val dataMuestrasNumero = binding.muestraNro.text.toString().toInt()
            val dataTipo = binding.spinnerTipo.selectedItem.toString()
            val dataStp1 = binding.inputStp1.text.toString().toInt()
            val dataStp2 = binding.inputStp2.text.toString().toInt()
            val dataStp3 = binding.inputStp3.text.toString().toInt()
            val dataSucsIntermedio = binding.spinnerSucs.selectedItem.toString()
            val dataSucs: Sucs= Sucs.CH
            val dataNivelFreatico = binding.valorNf.toString().toDouble()
            val dataDescripcion = binding.descripcion.text.toString()
             val pf = Profundidad(-1,-1,dataProfundidad,dataMuestrasNumero,dataTipo
             ,dataStp1,dataStp2,dataStp3,dataSucs,dataDescripcion,dataNivelFreatico)
        }
    }
    private  fun  chequedoDeDatos(): Boolean{
        return binding.profundidad.text?.isEmpty() == false && binding.muestraNro.text?.isEmpty() == false
                && binding.inputStp1.text?.isEmpty() == false && binding.inputStp2.text?.isEmpty() == false &&
                binding.inputStp3.text?.isEmpty() == false && binding.descripcion.text?.isEmpty() == false
    }
}