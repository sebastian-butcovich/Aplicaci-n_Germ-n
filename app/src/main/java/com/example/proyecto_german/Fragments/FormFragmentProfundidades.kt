package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_german.Adapters.Profundidad.ProfundiadAdapter
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentProfundidadesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.getValue

class FormFragmentProfundidades: Fragment() {
    private var _biding : FragmentProfundidadesBinding ? =null
    private val binding get() = _biding!!
    private val viewModel: PerforacionViewModel by activityViewModels {
        PeforacionViewModelFactory(
            PerforacionRepository(
                PerforacionesApplication.database.perforacionDao()
            )
        )
    }
    private lateinit var adapter: ProfundiadAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentProfundidadesBinding.inflate(inflater,container,false)
        binding.root.findViewById<FloatingActionButton>(R.id.button_floting_add).setOnClickListener {
            //Tengo que limpiar la lista de golpes y la profundidad

            findNavController().navigate(R.id.action_formFragmentProfundidades_to_formFragmentProfundidad)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observerAdapter()
        guardarDatos()
    }

    private fun guardarDatos() {
        binding.root.findViewById<Button>(R.id.boton_guardar).setOnClickListener {
            viewModel.agregarPerforacion()
            Toast.makeText(requireContext(),"Base de datos guardada",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView(){
        adapter = ProfundiadAdapter(emptyList(),
            onClickListener = {profundidad ->
                onItemSelected(profundidad)
            },
            onClickVerGolpes = {profundidad ->
                viewModel.abrirGolpesParaVisualizar(profundidad)
                findNavController().navigate(R.id.action_formFragmentProfundidades_to_formFragmentProfundidad)
            },
            onClickEditar = {profundidad -> },
            onClickEliminar = {profundidad ->

            })
        binding.listaProfundidad.layoutManager = LinearLayoutManager(requireContext())
        binding.listaProfundidad.adapter = adapter
    }
    fun onItemSelected(profundidad: Profundidad){
        Toast.makeText(requireContext(),profundidad.descripcion,Toast.LENGTH_SHORT).show()
    }
    private fun observerAdapter(){
        viewModel.profundidadesConGolpes.observe(viewLifecycleOwner){
            listaConGolpes->
            val listaProfundidades = listaConGolpes.map{it.profundidad}
            adapter.updateList(listaProfundidades)
        }
    }



}