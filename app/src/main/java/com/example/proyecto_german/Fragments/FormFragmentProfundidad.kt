package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.proyecto_german.Model.Sucs
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
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
        when(viewModel.modoProfundidad){
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
    }
    private fun inicializarValores(){
        //Existe la profundidad o no tenes nada que hacer
        val profundidad = viewModel.profundidadActual?:return
        binding.spinnerSucs.setSelection(
            obtenerPosicionSucs(profundidad.sucs)
        )
        binding.descripcion.setText(
            profundidad.descripcion
        )
        binding.spinnerSimbolo.setSelection(
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
            val golpes = viewModel.obtenerGolpesDeUnaProfundidad(idProfundidad)
            val hayGolpes = golpes.isNotEmpty()
            binding.checkGolpes.isChecked = !hayGolpes
            mostrarInputsProfundidades()
        }
    }
    private fun obtenerPosicionSimbolo(simbolo: String): Int{
        when(simbolo){
            "ARENA"->return 1
            "GRAVAS"->return 2
            "CL"->return 3
            "CH"->return 4
            "ML"->return 5
            "MH"->return 6
            "OL"->return 7
            "OH"->return 8
            "PT"->return 9

        }
        return 0
    }

    private fun obtenerPosicionSucs(sucs: Sucs):Int {
      return Sucs.entries.indexOf(sucs)
    }

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
            binding.root.findNavController().navigate(R.id.action_formFragmentProfundidad_to_formFragmentGolpes)
        }
    }
    private fun mostrarLista(){
        adapter = StpAdapter(emptyList(),
            onClickListener = { golpesStp ->
                onItemSelected(golpesStp)
            },
            editarGolpe = {golpesStp ->

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
            })
        binding.listaSpt.layoutManager = LinearLayoutManager(requireContext())
        binding.listaSpt.adapter = adapter
    }

    private fun onItemSelected(golpe: GolpesStp) {
        Toast.makeText(requireContext(),"Seleccionaste un item",Toast.LENGTH_SHORT).show()
    }

    private fun botonAgregar() {
        binding.root.findViewById<Button>(R.id.boton_guardar_profundidad).setOnClickListener {
            cargarDatosProfundidad()
        }
    }
    private fun cargarDatosProfundidad(){
        val primerGolpe = viewModel.golpesActuales.firstOrNull()
        val ultimoGolpe = viewModel.golpesActuales.lastOrNull()
        var profundidadInicial:Double? ;
        var profundidadFinal:Double?
        if(primerGolpe == null || ultimoGolpe == null){
            profundidadInicial = binding.profundidadInicialProfundidad.text.toString().toDoubleOrNull()
            profundidadFinal = binding.profundidadFinalProfundidad.text.toString().toDoubleOrNull()
        }else{
            profundidadInicial = primerGolpe.profundidad_inicial
            profundidadFinal = ultimoGolpe.profundidad_final
        }
        val profundidad = Profundidad(
            id=0, perforacionId =0,
            descripcion = binding.descripcion.text.toString(),
            simbolo = binding.spinnerSimbolo.selectedItem.toString(),
            sucs = Sucs.valueOf(
                binding.spinnerSucs.selectedItem.toString()
            ),
            profundidadFinal =  profundidadFinal,
            profundidadInicial = profundidadInicial
        )
        viewModel.profundidadActual = profundidad
        viewModel.confirmarProfundidadConGolpes()
        findNavController().popBackStack()
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
    }
}