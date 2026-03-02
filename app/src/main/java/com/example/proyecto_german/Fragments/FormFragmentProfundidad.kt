package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_german.Adapters.STP.StpAdapter
import com.example.proyecto_german.Data.Application.PerforacionesApplication
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarValores()
        botonAgregar()
        observerAdapter()
        accionarCheckBox()
        mostrarInputsProfundidades()
        agregarStpAccion()
        mostrarLista()
    }
    private fun inicializarValores(){
        val posicion = determinarValorSucs()
        binding.spinnerSucs.setSelection(posicion)
        binding.descripcion.setText(
            viewModel.profundidadActual?.descripcion
        )
        binding.spinnerSimbolo.setSelection(posicion)
        binding.checkGolpes.isChecked = !determinarSiHayGolpes()
        if(binding.checkGolpes.isChecked){
            accionarCheckBox()
        }
        binding.profundidadInicialProfundidad.setText(
            viewModel.profundidadActual?.profundidadInicial.toString())

        binding.profundidadFinalProfundidad.setText(
            viewModel.profundidadActual?.profundidadFinal.toString()
        )
    }
    private fun determinarSiHayGolpes():Boolean{
        var retorno = false
        lifecycleScope.launch {
            val lista = viewModel.obtenerGolpesDeUnaProfundidad(viewModel.profundidadActual!!.id)
            if(lista.isEmpty()){
                retorno = true
            }
        }
        return retorno
    }
    private fun determinarValorSucs():Int {
      val valor = binding.spinnerSucs.selectedItem
      if(valor == Sucs.VACIO.toString()){
          return 0
      }else if(valor == Sucs.CH.toString()){
          return 1
      }else if(valor == Sucs.MH.toString()) {
          return 2
      }else if(valor == Sucs.SM.toString()){
          return 3
      }else
          return 4
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
        adapter = StpAdapter(emptyList()){
            golpeStp->onItemSelected()
        }
        binding.listaSpt.layoutManager = LinearLayoutManager(requireContext())
        binding.listaSpt.adapter = adapter
    }

    private fun onItemSelected() {
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
}