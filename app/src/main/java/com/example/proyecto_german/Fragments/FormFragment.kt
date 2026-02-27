package com.example.proyecto_german.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        generarCalendario()
    }

    fun accionBoton(){
        val miBoton = binding.root.findViewById<Button>(R.id.boton_continuar)
        miBoton.setOnClickListener {
            if(chequeoDatosFormulario()){
                val perforacion = obtenerDatosDeLosInputs()
                viewModel.actulizarPerforacion(perforacion)
                findNavController().navigate(R.id.action_formFragment_to_formFragmentProfundidades)
            }else{
                Toast.makeText(context,"No ingresaste los dato mínimos del formulario",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun chequeoDatosFormulario(): Boolean{
        if(!binding.inputCliente.text.toString().isEmpty()
            && !binding.inputAtencion.text.toString().isEmpty()
            && !binding.inputProyecto.text.toString().isEmpty()
            && !binding.inputLocalizacion.text.toString().isEmpty()
            && binding.inputNumeroPerforacion.text.toString().toDouble() != 0.0
            && binding.inputProfundidad.text.toString().toDouble() != 0.0
            && binding.inputCoordenadaE.text.toString().toDouble() != 0.0
            && binding.inputCoordenadaN.text.toString().toDouble() != 0.0
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
        val localizacion = binding.inputLocalizacion.text.toString();
        Log.i("Fecha",fecha.toString())
        val numeroPerforacion = binding.inputNumeroPerforacion.text.toString().toInt()
        val profundidad = binding.inputProfundidad.text.toString().toDouble()
        val coordenadaX = binding.inputCoordenadaE.text.toString().toDouble()
        val coordenadaY = binding.inputCoordenadaN.text.toString().toDouble()
        val nivelFreatico = binding.spinnerNivelFreatico.selectedItem.toString() == "Si"
        val lecturaInicial = binding.inputLecturaInicial.text.toString().toDouble()
        val lecturaFinal = binding.inputLecturaFinal.text.toString().toDouble()
        val estadoTiempo = binding.inputEstadoTiempo.text.toString()
        var p= PerforacionModel(0,"",fecha,
            "",cliente,atencion,proyecto,localizacion,fecha,numeroPerforacion
            ,profundidad,coordenadaX,coordenadaY,
            nivelFreatico,lecturaInicial,lecturaFinal,estadoTiempo)
        return p

    }

    private fun generarCalendario(){
        val calendario = Calendar.getInstance()
        var fecha = DatePickerDialog.OnDateSetListener{ datePicker, anio, mes, dia
            -> calendario.set(Calendar.YEAR, anio)
            calendario.set(Calendar.MONTH,mes)
            calendario.set(Calendar.DAY_OF_MONTH,dia)
            pasarDatosAlInputFecha(calendario)
        }
        binding.inputFecha.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                fecha,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }
    private fun pasarDatosAlInputFecha(calendario: Calendar){
        val formatoFecha = "dd-MM-yyyy"
        val formatoSimple = SimpleDateFormat(formatoFecha, Locale.ENGLISH)
        binding.inputFecha.setText(formatoSimple.format(calendario.time))
    }
}