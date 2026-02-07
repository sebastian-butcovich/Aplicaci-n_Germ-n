package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import kotlin.getValue
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory

class FormFragment: Fragment() {
    private var _biding : FragmentFormularioPerforacionBinding? =null
    private val binding get() = _biding!!
    val dao = PerforacionesApplication.database.perforacionDao()
    val repository = PerforacionRepository(dao)
    val factory = PeforacionViewModelFactory(repository)
    private val viewModel: PerforacionViewModel by activityViewModels {
        PeforacionViewModelFactory(
            PerforacionRepository(
                PerforacionesApplication.database.perforacionDao()
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioPerforacionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accionBoton()
    }

    fun accionBoton(){
        val miBoton = binding.root.findViewById<Button>(R.id.boton_continuar)
        miBoton.setOnClickListener {
            if(chequeoDatosFormulario()){
                val perforacion = obtenerDatosDeLosInputs()
                viewModel.actulizarPerforacion(perforacion)
                findNavController().navigate(R.id.action_formFragment_to_formFragmentProfundidad)
            }else{
                Toast.makeText(context,"No ingresaste los dato mínimos del formulario",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun chequeoDatosFormulario(): Boolean{
        if(!binding.inputCliente.text.toString().isEmpty()
            && !binding.inputAtencion.text.toString().isEmpty()
            && !binding.inputProyecto.text.toString().isEmpty()
            && binding.inputNumeroPerforacion.text.toString().toDouble() != 0.0
            && binding.inputProfundidad.text.toString().toDouble() != 0.0
            && binding.inputCoordenadaX.text.toString().toDouble() != 0.0
            && binding.inputCoordenadaY.text.toString().toDouble() != 0.0
            && !binding.inputLecturaInicial.text.toString().isEmpty()
            && !binding.inputLecturaFinal.text.toString().isEmpty()
            && !binding.inputEstadoTiempo.text.toString().isEmpty()){
            return true
        }else{
            return false
        }
    }
    private fun obtenerDatosDeLosInputs(): PerforacionModel {
        val cliente = binding.inputCliente.text.toString()
        Log.i("Cliente",cliente)
        val atencion = binding.inputAtencion.text.toString()
        Log.i("Atención",atencion)
        val proyecto = binding.inputProyecto.text.toString()
        Log.i("Proyecto",proyecto)
        val fecha = java.util.Date()
        Log.i("Fecha",fecha.toString())
        val numeroPerforacion = binding.inputNumeroPerforacion.text.toString().toInt()
        val profundidad = binding.inputProfundidad.text.toString().toDouble()
        val coordenadaX = binding.inputCoordenadaX.text.toString().toDouble()
        val coordenadaY = binding.inputCoordenadaY.text.toString().toDouble()
        val nivelFreatico = obtenerValorLogicoFreatico()
        val lecturaInicial = binding.inputLecturaInicial.text.toString().toDouble()
        val lecturaFinal = binding.inputLecturaFinal.text.toString().toDouble()
        val estadoTiempo = binding.inputEstadoTiempo.text.toString()
        var p= PerforacionModel(0,"",fecha,
            "",cliente,atencion,proyecto,"",fecha,numeroPerforacion
            ,profundidad,coordenadaX,coordenadaY,
            nivelFreatico,lecturaInicial,lecturaFinal,estadoTiempo)
        return p

    }

    private fun obtenerValorLogicoFreatico(): Boolean {
       val r= binding.eleccion.isSelected.toString()
        return r == "Sí"
    }
}