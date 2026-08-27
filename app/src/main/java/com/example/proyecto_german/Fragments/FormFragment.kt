package com.example.proyecto_german.Fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.Perforacion
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.Util.configurarLimitesMaximoDouble
import com.example.proyecto_german.Util.mostrarDialogoError
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import kotlin.getValue
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
        limpiarFormulario()
        generarCalendario()
        if(viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.EDITAR){
            cambiarAEditar()
            inicializarInputs()
        }
        val opcionesFreatico: Array<String> = resources.getStringArray(R.array.valores_freatico)
        val adapterFreatico = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            opcionesFreatico
        )
        binding.spinnerNivelFreatico.setAdapter(adapterFreatico)
        limitarCampos()
    }
    private fun limitarCampos(){
        //Obtengo el input de profundidad
        binding.inputProfundidad.configurarLimitesMaximoDouble(6,0,30.0);
        binding.inputNumeroPerforacion.configurarLimitesMaximoDouble(6,1,50)
        binding.inputCoordenadaE.configurarLimitesMaximoDouble(6,-180,180)
        binding.inputCoordenadaN.configurarLimitesMaximoDouble(6,-90,90)
        binding.inputLecturaInicial.configurarLimitesMaximoDouble(6,0,30)
        binding.inputLecturaFinal.configurarLimitesMaximoDouble(6,0,30)
    }
    private fun limpiarFormulario() {
        binding.inputCliente.setText("")
        binding.inputProyecto.setText("")
        binding.inputAtencion.setText("")
        binding.inputProfundidad.setText("")
        binding.inputEstadoTiempo.setText("")
        binding.inputLecturaInicial.setText("")
        binding.inputLecturaFinal.setText("")
        binding.inputCoordenadaN.setText("")
        binding.inputCoordenadaE.setText("")
        binding.inputNumeroPerforacion.setText("")
        binding.inputLocalizacion.setText("")
        binding.spinnerNivelFreatico.setText("No",false)
    }

    private fun cambiarAEditar() {
        binding.botonContinuar.visibility = View.GONE
        binding.botonEditar.visibility = View.VISIBLE
    }

    private fun inicializarInputs(){
       try{
           val perforacion = viewModel.perforacionEdit ?:return
           binding.inputCliente.setText(perforacion.cliente)
           binding.inputAtencion.setText(perforacion.atencion)
           binding.inputProyecto.setText(perforacion.proyecto)
           binding.inputLocalizacion.setText(perforacion.localizacion)
           binding.inputNumeroPerforacion.setText(perforacion.numeroPerforacion.toString())
           binding.inputProfundidad.setText(perforacion.profundidadMetros.toString())
           binding.inputCoordenadaE.setText(perforacion.coordenadaE.toString())
           binding.inputCoordenadaN.setText(perforacion.coordenadaN.toString())
           binding.inputFecha.setText(perforacion.fecha.aTextoFormato())
           val opciones = resources.getStringArray(R.array.valores_freatico)
           if (perforacion.nivelFreatico) {
               binding.spinnerNivelFreatico.setText(opciones[1], false) // Muestra "Sí"
           } else {
               binding.spinnerNivelFreatico.setText(opciones[0], false) // Muestra "No"
           }
           binding.inputLecturaInicial.setText(perforacion.lecturaInicial.toString())
           binding.inputLecturaFinal.setText(perforacion.lecturaFinal.toString())
           binding.inputEstadoTiempo.setText(perforacion.estadoDelTiempo)
       }catch(e: Exception){
           mostrarDialogoError("Error al cargar la perforación","Error al cargar la perforación",requireContext())
       }finally {
           //Por el momento no tengo nada definido.
       }
    }
    fun accionBoton(){
        val miBoton = binding.root.findViewById<Button>(R.id.boton_continuar)
        miBoton.setOnClickListener {
//            if(chequeoDatosFormulario()){
                val perforacion = obtenerDatosDeLosInputs()
                if(perforacion != null) {
                    viewModel.actulizarPerforacion(perforacion)
                    viewModel.limpiarProfundidades()
                    findNavController().navigate(R.id.action_formFragment_to_formFragmentProfundidades)
                }
//            }else{
//                Toast.makeText(context,"No ingresaste los dato mínimos del formulario",Toast.LENGTH_SHORT).show()
//            }
        }
        binding.root.findViewById<Button>(R.id.boton_editar).setOnClickListener {
            //if(chequeoDatosFormulario()){
                val perforacion = obtenerDatosDeLosInputs()
                if(perforacion != null){
                    viewModel.actulizarPerforacion(perforacion)
                }
         //   }else{
             //   Toast.makeText(context,"No ingresaste los dato mínimos del formulario",Toast.LENGTH_SHORT).show()
           // }
        }
    }
    private fun chequeoDatosFormulario(): Boolean{
        if(!binding.inputCliente.text.toString().isEmpty()
            && !binding.inputAtencion.text.toString().isEmpty()
            && !binding.inputProyecto.text.toString().isEmpty()
            && !binding.inputLocalizacion.text.toString().isEmpty()
            && binding.inputNumeroPerforacion.text.toString().isEmpty()
            && binding.inputProfundidad.text.toString().isEmpty()
            && binding.inputCoordenadaE.text.toString().isEmpty()
            && binding.inputCoordenadaN.text.toString().isEmpty()
            && !binding.inputLecturaInicial.text.toString().isEmpty()
            && !binding.inputLecturaFinal.text.toString().isEmpty()
            && !binding.inputEstadoTiempo.text.toString().isEmpty()){
            return true
        }else{
            return false
        }
    }
    private fun obtenerDatosDeLosInputs(): Perforacion? {
        try{
            val cliente = binding.inputCliente.text.toString().trim().ifBlank { "PENDIENTE" }
            val atencion = binding.inputAtencion.text.toString().trim().ifBlank { "PENDIENTE" }
            val proyecto = binding.inputProyecto.text.toString().trim().ifBlank { "PENDIENTE" }
            val localizacion = binding.inputLocalizacion.text.toString().trim().ifBlank { "PENDIENTE" };
            val fechaString = binding.inputFecha.text.toString().trim().ifBlank{" "}
            val fecha = parsearFechaODefecto(fechaString)
            val numeroPerforacion = binding.inputNumeroPerforacion.text.toString().trim().toDoubleOrNull()?:0.0
            val profundidad = binding.inputProfundidad.text.toString().trim().toDoubleOrNull()?:0.0
            val coordenadaX = binding.inputCoordenadaE.text.toString().trim().toDoubleOrNull()?:0.0
            val coordenadaY = binding.inputCoordenadaN.text.toString().trim().toDoubleOrNull()?:0.0
            val nivelFreatico = binding.spinnerNivelFreatico.text.toString() == "Si"
            val lecturaInicial = binding.inputLecturaInicial.text.toString().trim().toDoubleOrNull()?:0.0
            val lecturaFinal = binding.inputLecturaFinal.text.toString().trim().toDoubleOrNull()?:0.0
            val estadoTiempo = binding.inputEstadoTiempo.text.toString().trim().ifBlank { "PENDIENTE" }
            if(viewModel.perforacionEdit != null && viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.EDITAR ){
                val p= Perforacion(
                    viewModel.perforacionEdit!!.id,"",fecha,
                    "",cliente,atencion,proyecto,localizacion,fecha,numeroPerforacion
                    ,profundidad,coordenadaX,coordenadaY,
                    nivelFreatico,lecturaInicial,lecturaFinal,estadoTiempo)
                viewModel.actualizarPerforacion(p)
                viewModel.obtenerPerforaciones()
                viewModel.modoProfundidadActual = viewModel.modoProfundidadAnterior
                findNavController().popBackStack()
                return p
            }else{
                val p= Perforacion(0,"",fecha,
                    "",cliente,atencion,proyecto,localizacion,fecha,numeroPerforacion
                    ,profundidad,coordenadaX,coordenadaY,
                    nivelFreatico,lecturaInicial,lecturaFinal,estadoTiempo)
                return p
            }
        }catch(e: Exception) {
            mostrarDialogoError(
                "Error al cargar la perforación",
                "Error al obtener los datos de la perforación",
                requireContext()
            )
        }
        return null
    }
    private fun parsearFechaODefecto(fecha: String,formato:String = "dd-MM-yyyy"): Date{
        if(fecha.isBlank()) return Date();
        return try{
            val sdf = SimpleDateFormat(formato, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(fecha.trim())?:Date()
        }catch(e: Exception){
            Date() // Si el formato ingresado es invalido, regresa la fecha actual.
        }
    }
    private fun generarCalendario(){
        val calendario = Calendar.getInstance()
        var fecha = DatePickerDialog.OnDateSetListener{ datePicker, anio, mes, dia
            -> calendario.set(Calendar.YEAR, anio)
            calendario.set(Calendar.MONTH,mes)
            calendario.set(Calendar.DAY_OF_MONTH,dia)
            pasarDatosAlInputFecha(calendario)
        }
        binding.inputFecha.isFocusable = false
        binding.inputFecha.isClickable = true
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
        val formatoSimple = SimpleDateFormat(formatoFecha, Locale.getDefault())
        binding.inputFecha.setText(formatoSimple.format(calendario.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.modoProfundidadActual = PerforacionViewModel.ModoProfundidad.CREAR
    }
    //Función que me permite pasar mi fecha de base de datos a una fecha string "yyyy/mm/dd"
     fun Date?.aTextoFormato():String{
        if(this == null) return ""
        val formato = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return formato.format(this)
    }
}