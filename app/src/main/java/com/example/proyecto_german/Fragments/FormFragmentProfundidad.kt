package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Adapters.Profundidad.ProfundiadAdapter
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionProfundidadBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.getValue

class FormFragmentProfundidad: Fragment() {
    private var _biding : FragmentFormularioPerforacionProfundidadBinding? =null
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
        _biding = FragmentFormularioPerforacionProfundidadBinding.inflate(inflater,container,false)
        binding.root.findViewById<FloatingActionButton>(R.id.button_floting_add).setOnClickListener {
            findNavController().navigate(R.id.action_formFragmentProfundidad_to_formFragmentSTP2)
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
        adapter = ProfundiadAdapter(emptyList()){
            profundidad -> onItemSelected(profundidad)
        }
        binding.listaSpt.layoutManager = LinearLayoutManager(requireContext())
        binding.listaSpt.adapter = adapter
    }
    fun onItemSelected(profundidad: Profundidad){
        Toast.makeText(requireContext(),profundidad.descripcion,Toast.LENGTH_SHORT).show()
    }
    private fun observerAdapter(){
        viewModel.profundidades.observe(viewLifecycleOwner){
            lista->
            adapter.updateList(lista)
        }
    }



}