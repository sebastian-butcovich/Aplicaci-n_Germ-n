package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Sucs
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.ViewModel.PeforacionViewModelFactory
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.FragmentFormularioSptBinding
import kotlin.getValue

class FormFragmentSTP : Fragment() {
    private var _biding: FragmentFormularioSptBinding? = null
    private val binding get() = _biding!!

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
        _biding = FragmentFormularioSptBinding.inflate(inflater, container, false)
        setupButton()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupButton() {
        binding.botonGuardar.setOnClickListener {
            sendDataToServer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun sendDataToServer() {
        if (chequedoDeDatos()) {
            val dataProfundidad = binding.profundidad.text.toString().toDouble()
            val dataMuestrasNumero = binding.muestraNro.text.toString().toInt()
            val dataTipo = binding.spinnerTipo.selectedItem.toString()
            val dataStp1 = binding.inputStp1.text.toString().toInt()
            val dataStp2 = binding.inputStp2.text.toString().toInt()
            val dataStp3 = binding.inputStp3.text.toString().toInt()
            val dataSucs: Sucs = Sucs.CH
            //val dataNivelFreatico = binding.valorNf.text.toString().toDouble()
            val dataDescripcion = binding.descripcion.text.toString()
            val pf = Profundidad(
                0,
                0,
                dataProfundidad,
                dataMuestrasNumero,
                dataTipo,
                dataStp1,
                dataStp2,
                dataStp3,
                dataSucs,
                dataDescripcion,
                //dataNivelFreatico
            )
            //Con esto ya estoy guardando una entrada.
            viewModel.updateListProfundidades(pf)
            findNavController().popBackStack()

        }else{
            Toast.makeText(context,"Faltan los datos mínimos para cargar un profundidad", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.profundidad.text = null
        binding.muestraNro.text = null
        binding.inputStp1.text = null
        binding.inputStp2.text = null
        binding.inputStp3.text =  null
        //binding.valorNf.text = null
        binding.descripcion.text = null
    }
    private fun chequedoDeDatos(): Boolean {
        return binding.profundidad.text?.isEmpty() == false && binding.muestraNro.text?.isEmpty() == false
                && binding.inputStp1.text?.isEmpty() == false && binding.inputStp2.text?.isEmpty() == false &&
                binding.inputStp3.text?.isEmpty() == false && binding.descripcion.text?.isEmpty() == false
    }
}