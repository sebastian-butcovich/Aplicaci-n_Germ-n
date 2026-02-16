package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_german.Adapters.Perforacion.PerforacionAdapter
import com.example.proyecto_german.Adapters.Profundidad.ProfundiadAdapter
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.Util.ManipularExcel
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlin.getValue

class HomeFragment: Fragment() {
    private var _biding : FragmentHomeBinding? =null
    private val binding get() = _biding!!
    private lateinit var adapter: PerforacionAdapter
    //Con este view voy a poder obtener la lista de perforaciones
    private val viewModel: PerforacionViewModel by activityViewModels {
        PeforacionViewModelFactory(
            PerforacionRepository(
                PerforacionesApplication.database.perforacionDao()
            )
        )
    }
    private val manipularExcel = ManipularExcel()
     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
         _biding = FragmentHomeBinding.inflate(inflater,container,false)
         initRecyclerView()
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initRecyclerView()
        observarPerforaciones()
        viewModel.obtenerPerforaciones()
        binding

    }
    private fun initRecyclerView(){
        adapter = PerforacionAdapter(emptyList(),
            onClickListener = {perforacion->
                onItemSelected(perforacion)
            },
            onExportClick = {perforacion->
                lifecycleScope.launch {
                   val profundidades = viewModel.obtenerProfundidadesDeUnaPerforacion(perforacion.id)
                    val manipularExcel= ManipularExcel()
                    manipularExcel.exportarSTP(requireContext()
                        ,perforacion, profundidades )
                }

            })
        binding.listaPerforaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.listaPerforaciones.adapter = adapter
    }
    private fun onItemSelected(perforacion: PerforacionModel){
        Toast.makeText(requireContext(),perforacion.proyecto,Toast.LENGTH_SHORT).show()
    }
    private fun observarPerforaciones(){
        viewModel.perforaciones.observe(viewLifecycleOwner){
            lista-> adapter.actualizarListaPerforacion(lista)
        }
    }
}