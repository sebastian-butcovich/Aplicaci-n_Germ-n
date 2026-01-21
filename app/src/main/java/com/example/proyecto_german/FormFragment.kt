package com.example.proyecto_german

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionProfundidadBinding

class FormFragment: Fragment() {
    private var _biding :FragmentFormularioPerforacionBinding? =null
    private val binding get() = _biding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentFormularioPerforacionBinding.inflate(inflater,container,false)
        accionBoton()
        return binding.root
    }
    fun accionBoton(){
        print("LLegue aca 1")
        val formFragment = FormFragmentProfundidad()
        val miBoton = binding.root.findViewById<Button>(R.id.boton_agregar_profundiad)
        miBoton.setOnClickListener {
            // Código al hacer clic
//            Toast.makeText(requireContext(), "¡Botón clickeado!", Toast.LENGTH_SHORT).show()
            if(formFragment.isVisible){
                print("Llegue aca")
                childFragmentManager.beginTransaction()
                    .remove( formFragment).commit()
            }else{
                print(" O llegue aca")
                childFragmentManager.beginTransaction()
                    .replace(R.id.contenedor_frame_layout,formFragment).commit()
            }
        }
    }
}