package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentGolpesStpBinding
import kotlin.getValue

class FormFragmentGolpes: Fragment() {
    private var _binding: FragmentGolpesStpBinding? =null
    private val binding get() = _binding!!
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
        _binding = FragmentGolpesStpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.findViewById<Button>(R.id.boton_guardar_golpe).setOnClickListener {
            guardarEnLaListaStp()
        }
    }

        private fun guardarEnLaListaStp() {
        if (chequedoDeDatos()) {
            val dataProfundidadInicial = binding.profundidadInicial.text.toString().toDouble()
            val dataProfundidadFinal = binding.profundidadFinal.text.toString().toDouble()
            val dataMuestrasNumero = binding.muestraNro.text.toString().toDoubleOrNull()
            val dataTipo = binding.spinnerTipo.selectedItem.toString()
            val dataStp1 = binding.inputStp1.text.toString().toIntOrNull()
            val dataStp2 = binding.inputStp2.text.toString().toIntOrNull()
            val dataStp3 = binding.inputStp3.text.toString().toIntOrNull()
            val pf = GolpesStp(
                0,
                0,
                dataProfundidadInicial,
                dataProfundidadFinal,
                dataMuestrasNumero,
                dataTipo,
                dataStp1,
                dataStp2,
                dataStp3,
            )
            //Con esto ya estoy guardando una entrada.
            viewModel.agregarGolpe( pf)
            findNavController().popBackStack()

        }else{
            Toast.makeText(context,"Faltan los datos mínimos para cargar un profundidad, mínimamente es necesario" +
                    "cargar la profundidad inicial y final", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.profundidadInicial.text = null
        binding.profundidadFinal.text = null
        binding.muestraNro.text = null
        binding.inputStp1.text = null
        binding.inputStp2.text = null
        binding.inputStp3.text =  null
    }
    private fun chequedoDeDatos(): Boolean {
        return binding.profundidadFinal.text?.isEmpty() == false
                && binding.profundidadInicial.text?.isEmpty() == false/* binding.muestraNro.text?.isEmpty() == false
                && binding.inputStp1.text?.isEmpty() == false && binding.inputStp2.text?.isEmpty() == false &&
                binding.inputStp3.text?.isEmpty() == false &&*/
    }
}