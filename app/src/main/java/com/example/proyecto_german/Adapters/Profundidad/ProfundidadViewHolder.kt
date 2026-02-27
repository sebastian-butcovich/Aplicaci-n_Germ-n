package com.example.proyecto_german.Adapters.Profundidad

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.databinding.ItemProfundidadBinding

class ProfundidadViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemProfundidadBinding.bind(view)
    fun render(profundidad: Profundidad, onClickListener:(Profundidad)-> Unit){
        binding.itemProfundidadesInicial.text = "Prof. Inicial: " + profundidad.profundidadInicial +" mts"
        binding.itemProfundidadesFinal.text =  " Prof. Final: " + profundidad.profundidadFinal +" mts"
        binding.sucs.text = "SUCS: "+ profundidad.sucs.toString()
        binding.descripcion.text = "Descripción: "+profundidad.descripcion
    }
}