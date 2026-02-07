package com.example.proyecto_german.Adapters.Profundidad

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.databinding.ItemProfundidadBinding

class ProfundidadViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemProfundidadBinding.bind(view)
    fun render(profundidad: Profundidad, onClickListener:(Profundidad)-> Unit){
        binding.profundiadMts.text = "Profundidad en mts: "+profundidad.profundidad.toString()
        binding.numeroMuestra.text = "Número de muestra: "+profundidad.numero_muestra.toString()
        binding.tipo.text = "Tipo: "+profundidad.tipo
        binding.avanceStp1.text = "STP 1: " + profundidad.golpes1
        binding.avanceStp2.text = "STP 2: " + profundidad.golpes2
        binding.avanceStp3.text = "STP 3: " + profundidad.golpes3
        binding.sucs.text = "SUCS: "+ profundidad.sucs.toString()
        binding.descripcion.text = "Descripción: "+profundidad.descripcion
    }
}