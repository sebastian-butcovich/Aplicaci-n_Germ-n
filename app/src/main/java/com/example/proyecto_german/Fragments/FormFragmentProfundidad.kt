package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_german.Adapters.STP.StpAdapter
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.Profundidad
//import com.example.proyecto_german.Model.Sucs
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.Util.configurarLimitesMaximoDouble
import com.example.proyecto_german.Util.mostrarDialogoError
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentProfundidadBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import kotlin.getValue

class FormFragmentProfundidad : Fragment() {

    private var _binding: FragmentProfundidadBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerforacionViewModel by activityViewModels {
        PeforacionViewModelFactory(
            PerforacionRepository(
                PerforacionesApplication.database.perforacionDao()
            )
        )
    }
    private lateinit var adapter: StpAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfundidadBinding.inflate(inflater, container, false)
        when(viewModel.modoProfundidadActual){
            PerforacionViewModel.ModoProfundidad.CREAR -> habilitarEdicion(true)
            PerforacionViewModel.ModoProfundidad.EDITAR -> habilitarEdicion(true)
            PerforacionViewModel.ModoProfundidad.VER -> habilitarEdicion(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostrarLista()
        observerAdapter()
        inicializarValores()
        botonAgregar()
        accionarCheckBox()
        mostrarInputsProfundidades()
        agregarStpAccion()
        //En caso de que solo quiera ver los golpes
        if(viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.VER){
            ocultarBotonoesEnCasoDeNoEditar()
        }
        inicializarSpinners()
        filtrarCampos()
    }
    private fun filtrarCampos(){
        binding.profundidadInicialProfundidad.configurarLimitesMaximoDouble(6,0,30)
        binding.profundidadFinalProfundidad.configurarLimitesMaximoDouble(6,0,30)

    }
    private fun inicializarSpinners(){
        val opcionesSucs: Array<String> = resources.getStringArray(R.array.valores_sucs)
        val opcionesSimbolo: Array<String> = resources.getStringArray(R.array.valores_simbolo)
        val adapterSucs = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            opcionesSucs
        )
        val adapterSimbolo = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            opcionesSimbolo
        )
        binding.spinnerSucs.setAdapter(adapterSucs)
        binding.spinnerSimbolo.setAdapter(
            adapterSimbolo
        )
    }
    private fun ocultarBotonoesEnCasoDeNoEditar() {

        if(viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.VER){
            binding.botonGuardarProfundidad.visibility = View.GONE;
            binding.buttonFlotingAddStp.visibility = View.GONE
        }
    }

    private fun inicializarValores(){
        //Existe la profundidad o no tenes nada que hacer
        val profundidad = viewModel.profundidadActual?:return
        binding.spinnerSucs.setText(
            profundidad.sucs
        )
        binding.descripcion.setText(
            profundidad.descripcion
        )
        binding.spinnerSimbolo.setText(
            obtenerPosicionSimbolo(profundidad.simbolo)
        )

        if(binding.checkGolpes.isChecked){
            accionarCheckBox()
        }
        binding.profundidadInicialProfundidad.setText(
            profundidad.profundidadInicial?.toString()?:"")

        binding.profundidadFinalProfundidad.setText(
            profundidad.profundidadFinal?.toString()?:""
        )
        cargarEstadosGolpes(profundidad.id)
    }
    private fun cargarEstadosGolpes(idProfundidad: Long){
        lifecycleScope.launch {
            val golpes = if(idProfundidad == 0L){
                viewModel.golpesActuales
            }else{
                viewModel.obtenerGolpesDeUnaProfundidad(idProfundidad)
            }

            val hayGolpes = golpes.isNotEmpty()
            binding.checkGolpes.isChecked = !hayGolpes
            mostrarInputsProfundidades()
        }
    }
    private fun obtenerPosicionSimbolo(simbolo: String): String{
        when(simbolo){
            "ARENA"->return simbolo
            "GRAVAS"->return simbolo
            "CL"->return simbolo
            "CH"->return simbolo
            "ML"->return simbolo
            "MH"->return simbolo
            "OL"->return simbolo
            "OH"->return simbolo
            "PT"->return simbolo

        }
        return "VACIO"
    }

//    private fun obtenerPosicionSucs(sucs:String):String {
//        val arraySucs: Array<String> = resources.getStringArray(R.array.valores_sucs)
//        //val aux =  Sucs.entries.indexOf(sucs)
//        return arraySucs[aux]
//    }

    private fun accionarCheckBox() {
        val check = binding.root.findViewById<CheckBox>(R.id.checkGolpes)
       check.setOnClickListener {
          mostrarInputsProfundidades()
       }
    }

    private fun mostrarInputsProfundidades() {
        val check = binding.root.findViewById<CheckBox>(R.id.checkGolpes)
        val inputProfundidadIncial = binding.root.findViewById<TextInputEditText>(R.id.profundidadInicialProfundidad)
        val contenedorProfundidadInicial = binding.root.findViewById<TextInputLayout>(R.id.contenedorProfundidadInicialProfundidad)
        val contenedorProfundidadFinal = binding.root.findViewById<TextInputLayout>(R.id.contenedorProfundidadFinalProfundidad)
        val inputProfundidadFinal = binding.root.findViewById<TextInputEditText>(R.id.profundidadFinalProfundidad)
        val botonAgregarStp = binding.root.findViewById<FloatingActionButton>(R.id.button_floting_add_stp)
        inputProfundidadIncial.visibility = View.GONE
        inputProfundidadFinal.visibility = View.GONE
        if(check.isChecked){
            inputProfundidadIncial.visibility = View.VISIBLE
            inputProfundidadFinal.visibility = View.VISIBLE
            contenedorProfundidadFinal.visibility = View.VISIBLE
            contenedorProfundidadInicial.visibility = View.VISIBLE
            botonAgregarStp.visibility = View.GONE
        }else{

            inputProfundidadIncial.visibility = View.GONE
            contenedorProfundidadFinal.visibility = View.GONE
            contenedorProfundidadInicial.visibility = View.GONE
            inputProfundidadFinal.visibility = View.GONE
            botonAgregarStp.visibility = View.VISIBLE
        }
    }

    private fun agregarStpAccion(){
        binding.buttonFlotingAddStp.setOnClickListener {
            viewModel._golpeActual.value = null
            viewModel.modoProfundidadAnterior = viewModel.modoProfundidadActual
            viewModel.modoProfundidadActual = PerforacionViewModel.ModoProfundidad.CREAR
            binding.root.findNavController().navigate(R.id.action_formFragmentProfundidad_to_formFragmentGolpes)
        }
    }
    private fun mostrarLista(){
        adapter = StpAdapter(emptyList(),
            onClickListener = { golpesStp ->
                onItemSelected(golpesStp)
            },
            editarGolpe = {golpesStp ->
                viewModel.seleccionarGolpe(golpesStp)
                viewModel.modoProfundidadAnterior = viewModel.modoProfundidadActual
                viewModel.modoProfundidadActual = PerforacionViewModel.ModoProfundidad.EDITAR
                findNavController().navigate(
                    R.id.action_formFragmentProfundidad_to_formFragmentGolpes
                )
            },
            eliminarGolpe = {golpesStp ->
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Eliminar golpe")
                builder.setMessage("¿Desea eliminar este golpe?")
                builder.setPositiveButton("Sí"){
                    dialog, which ->
                    viewModel.eliminarGolpe(golpesStp)
                }
                builder.setNegativeButton("No"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            },
            viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.VER)
        binding.listaSpt.layoutManager = LinearLayoutManager(requireContext())
        binding.listaSpt.adapter = adapter
    }

    private fun onItemSelected(golpe: GolpesStp) {
        Toast.makeText(requireContext(),"Seleccionaste un item",Toast.LENGTH_SHORT).show()
    }

    private fun botonAgregar() {
        binding.root.findViewById<Button>(R.id.boton_guardar_profundidad).setOnClickListener {
            val prof = obtenerDatosInputs()
            if((viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.EDITAR
                /*|| chequearConsistenciaProfundidad(prof)*/
                || viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.CREAR
                        )&& prof != null){
                cargarDatosProfundidad(prof)
            }else{
                mostrarMensajeProfundidadInconsistente()
            }
        }
    }
    private fun chequearConsistenciaProfundidad(profActual: Profundidad): Boolean{
        //Los datos deben estar en mi profundida actual y en mi profundidadConGolpes
        val listaProfundidad = viewModel.profundidadesConGolpes.value
        if (listaProfundidad != null
            && profActual.profundidadInicial !=null
            && profActual.profundidadFinal !=null) {
            for (profundidadConGolpe in listaProfundidad) {
                val profundidad = profundidadConGolpe.profundidad
                if (profundidad.profundidadFinal != null
                    && profundidad.profundidadInicial != null && profActual.id != profundidad.id
                ) {
                    if(profActual.profundidadInicial >= profundidad.profundidadInicial && profActual.profundidadInicial <= profundidad.profundidadFinal){
                        return false
                    }
                    if(profActual.profundidadFinal >= profundidad.profundidadInicial && profActual.profundidadFinal <= profundidad.profundidadFinal){
                        return false
                    }
                    if(profActual.profundidadInicial >= profundidad.profundidadInicial && profActual.profundidadFinal <= profundidad.profundidadFinal){
                        return false;
                    }
                }
            }
        }
        return true
    }
    private fun mostrarMensajeProfundidadInconsistente(){
        mostrarDialogoError(
            "Inconsistencia de profundidades",
            "Una profundidad nueva no puede estar sobre otra profundidad." +
                    "La profundidad nueva debe tener una profundidad incial y final menor a todas la profundidades" +
                    "y sino debe tener una profundidad incial y fianl mayor a todas la profundidades",
            requireContext()
        )
    }
    private fun cargarDatosProfundidad(profundidad: Profundidad){
        if(viewModel.modoProfundidadActual == PerforacionViewModel.ModoProfundidad.CREAR){
            viewModel.profundidadActual = profundidad
        }else{
            val golpes = viewModel.golpesActuales.filter {
                it.id == 0L
            }.map {
                golpe->
                    golpe.copy(
                        id= golpe.id,
                        profundidadId = profundidad.id,
                        profundidad_inicial = golpe.profundidad_inicial,
                        profundidad_final = golpe.profundidad_final,
                        numero_muestra = golpe.numero_muestra,
                        tipo = golpe.tipo,
                        golpes1 = golpe.golpes1,
                        golpes2 = golpe.golpes2,
                        golpes3 = golpe.golpes3
                    )

            }
            if(profundidad.id !=0L){
                viewModel.actuarlizarProfundidadBaseDatos(profundidad,golpes)
            }else{
                viewModel.actualizarProfundidadEnMemoria(profundidad)
            }
        }
        viewModel.confirmarProfundidadConGolpes()
        viewModel.modoProfundidadActual = viewModel.modoProfundidadAnterior
        findNavController().popBackStack()
    }
    private fun obtenerDatosInputs(): Profundidad? {
        try{
            val profundidadExistente = viewModel.profundidadActual
            val golpes = viewModel.golpesActuales
            var profundidadInicial: Double=100.0;
            var profundidadFinal: Double=0.0
            if(binding.profundidadInicialProfundidad.text.toString() != "" || binding.profundidadFinalProfundidad.text.toString() != ""){
                profundidadInicial = binding.profundidadInicialProfundidad.text.toString().trim().toDoubleOrNull()?:0.0
                profundidadFinal = binding.profundidadFinalProfundidad.text.toString().trim().toDoubleOrNull()?:0.0
            }
            for(golpe in golpes){
                if(golpe.profundidad_inicial < profundidadInicial){
                    profundidadInicial = golpe.profundidad_inicial
                }
                if(golpe.profundidad_final > profundidadFinal){
                    profundidadFinal = golpe.profundidad_final
                }
            }
            val descripcion = binding.descripcion.text.toString()
            val simbolo = binding.spinnerSimbolo.text.toString()
            val sucs = binding.spinnerSucs.text.toString().trim().ifEmpty { "VACIO" }

            val profundidad =
                if (viewModel.modoProfundidadActual != PerforacionViewModel.ModoProfundidad.CREAR) {
                    profundidadExistente!!.copy(
                        descripcion = descripcion,
                        simbolo = simbolo,
                        sucs = sucs,
                        profundidadFinal = profundidadFinal,
                        profundidadInicial = profundidadInicial
                    )

                } else {
                    Profundidad(
                        id = 0, perforacionId = 0,
                        descripcion = descripcion,
                        simbolo = simbolo,
                        sucs = sucs,
                        profundidadFinal = profundidadFinal,
                        profundidadInicial = profundidadInicial
                    )
                }
            return profundidad
        }catch(e: Exception){
            mostrarDialogoError(
                "Error al cargar la profundidad",
                "Error al obtener los datos de la profundidad, revisa que datos pusiste cuidadosamente",
                requireContext()
            )
        }
        return null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun observerAdapter() {
        viewModel.golpesLiveData.observe(viewLifecycleOwner) { lista ->
            adapter.actualizarLista(lista)
        }
    }
    private fun habilitarEdicion(habilitar:Boolean){
        binding.descripcion.isEnabled = habilitar
        binding.spinnerSucs.isEnabled = habilitar
        binding.spinnerSimbolo.isEnabled = habilitar
        binding.checkGolpes.isEnabled = habilitar
        binding.profundidadInicialProfundidad.isEnabled = habilitar
        binding.profundidadFinalProfundidad.isEnabled = habilitar
        binding.root.findViewById<FloatingActionButton>(R.id.button_floting_add_stp).isEnabled = habilitar
        if(!habilitar){
            binding.botonGuardarProfundidad.visibility = View.GONE
        }
    }
}