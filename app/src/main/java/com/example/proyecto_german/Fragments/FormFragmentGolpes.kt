package com.example.proyecto_german.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.proyecto_german.Data.Application.PerforacionesApplication
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.R
import com.example.proyecto_german.Repository.PerforacionRepository
import com.example.proyecto_german.Util.actualizarProfundidadFinalEnBaseAlInicial
import com.example.proyecto_german.Util.configurarLimitesMaximoDouble
import com.example.proyecto_german.Util.mostrarDialogoError
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
        if(viewModel.golpeActualLiveData.value == null){
            limpiarCampos()
        }
        observarGolpe()
        binding.root.findViewById<Button>(R.id.boton_guardar_golpe).setOnClickListener {
            guardarEnLaListaStp()
        }
        inializarSpinner()
        filtrarCampos()
    }
    private fun filtrarCampos(){
        binding.profundidadInicial.configurarLimitesMaximoDouble(6,0,30)
        binding.profundidadFinal.configurarLimitesMaximoDouble(6,0,30)
        binding.profundidadInicial.actualizarProfundidadFinalEnBaseAlInicial(binding.profundidadInicial,binding.profundidadFinal)
        binding.muestraNro.configurarLimitesMaximoDouble(6,0,30)
        binding.inputStp1.configurarLimitesMaximoDouble(6,0,100)
        binding.inputStp2.configurarLimitesMaximoDouble(6,0,100)
        binding.inputStp3.configurarLimitesMaximoDouble(6,0,100)
    }
    private fun inializarSpinner(){
        val opcionesTipo: Array<String> = resources.getStringArray(R.array.valores_tipo)
        val adapterTipo = ArrayAdapter<String> (
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            opcionesTipo
        )
        binding.spinnerTipo.setAdapter(adapterTipo)
    }

        private fun guardarEnLaListaStp() {
//            if (!chequedoDeDatos()) {
//                Toast.makeText(context,"Faltan los datos mínimos para cargar un profundidad, mínimamente es necesario" +
//                        "cargar la profundidad inicial y final", Toast.LENGTH_SHORT).show()
//            }else{
                val dataProfundidadInicial = binding.profundidadInicial.text.toString().toDoubleOrNull()?:0.0
                val dataProfundidadFinal = binding.profundidadFinal.text.toString().toDoubleOrNull()?:0.0
                val dataMuestrasNumero = binding.muestraNro.text.toString().toDoubleOrNull()?:0.0
                val dataTipo = binding.spinnerTipo.text.toString().trim().ifBlank { "VACIO" }
                val dataStp1 = binding.inputStp1.text.toString().toIntOrNull()?:0.0
                val dataStp2 = binding.inputStp2.text.toString().toIntOrNull()?:0.0
                val dataStp3 = binding.inputStp3.text.toString().toIntOrNull()?:0.0
                val golpeExistente = viewModel.golpeActualLiveData.value
                if(golpeExistente == null){
                    val pf = GolpesStp(
                        0,
                        0,
                        dataProfundidadInicial,
                        dataProfundidadFinal,
                        dataMuestrasNumero,
                        dataTipo,
                        dataStp1.toInt(),
                        dataStp2.toInt(),
                        dataStp3.toInt(),
                    )
//                    //Con esto ya estoy guardando una entrada.
//                    if(chequearGolpeConsistente(pf)) {
                        viewModel.agregarGolpe(pf)
                        viewModel.modoProfundidadActual = viewModel.modoProfundidadAnterior
                        findNavController().popBackStack()
//                    }else{
//                        mostrarDialogoError("Error golpe superpuesto",
//                            "Estas queriendo agregar un golpe que ya está ocupando esas profundidades.",requireContext())
//                    }
                }else{
                    val golpeActualizado = golpeExistente.copy(
                        id=golpeExistente.id,
                        profundidad_inicial = dataProfundidadInicial,
                        profundidad_final = dataProfundidadFinal,
                        tipo = dataTipo,
                        numero_muestra = dataMuestrasNumero,
                        golpes1 = dataStp1.toInt(),
                        golpes2 = dataStp2.toInt(),
                        golpes3 = dataStp3.toInt()
                        )
                    if(chequearGolpeConsistente(golpeActualizado)) {
                        viewModel.actualizarGolpe(golpeActualizado)
                        viewModel.modoProfundidadActual = viewModel.modoProfundidadAnterior
                        findNavController().popBackStack()
                    }else{
                        mostrarDialogoError("Error golpe superpuesto",
                            "Estas queriendo agregar un golpe que ya está ocupando esas profundidades.",requireContext())
                    }
                }
//            }
    }
    private fun chequearGolpeConsistente(golpe: GolpesStp): Boolean{
        val golpes = viewModel.golpesActuales
        for(gol in golpes){
            //La profundidad inicial del golpe nuevo es menor que la profundidad inicial del golpe
            //La profundida inicial
            if(gol.id != golpe.id){
                if(golpe.profundidad_inicial >= gol.profundidad_inicial && golpe.profundidad_inicial <= gol.profundidad_final){
                    return false
                }
                if(golpe.profundidad_final >= gol.profundidad_inicial && golpe.profundidad_final <= gol.profundidad_final){
                    return false
                }
                if(golpe.profundidad_inicial >= gol.profundidad_inicial && golpe.profundidad_final <= gol.profundidad_final){
                    return false;
                }
            }
        }
        return true
    }
    private fun limpiarCampos() {
        binding.profundidadInicial.setText("")
        binding.profundidadFinal.setText("")
        binding.muestraNro.setText("")
        binding.inputStp1.setText("")
        binding.inputStp2.setText("")
        binding.inputStp3.setText("")
    }
    private fun chequedoDeDatos(): Boolean {
        return binding.profundidadFinal.text?.isEmpty() == false
                && binding.profundidadInicial.text?.isEmpty() == false
    }

    private fun observarGolpe(){
        viewModel.golpeActualLiveData.observe(viewLifecycleOwner){
            golpe->
            golpe ?: return@observe
            binding.profundidadInicial.setText(
                golpe.profundidad_inicial.toString()
            )
            binding.profundidadFinal.setText(
                golpe.profundidad_final.toString()
            )
            binding.muestraNro.setText(
                golpe.numero_muestra?.toString()
            )
            binding.spinnerTipo.setText(golpe.tipo)
            binding.inputStp1.setText(
                golpe.golpes1?.toString()
            )
            binding.inputStp2.setText(
                golpe.golpes2?.toString()
            )
            binding.inputStp3.setText(
                golpe.golpes3?.toString()
            )
        }
    }
}