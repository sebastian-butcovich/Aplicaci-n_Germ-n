package com.example.proyecto_german.Adapters.Perforacion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.databinding.ItemPerforacionBinding

class PerforacionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemPerforacionBinding.bind(view)
    fun render(perforacion: PerforacionModel,onClickListener:(PerforacionModel)-> Unit){
        binding.proyecto.text = "Proyecto: "+perforacion.proyecto
        binding.cliente.text = "Cliente: "+perforacion.clinte
        binding.atencion.text = "Atención: "+ perforacion.atencion
        binding.profundiad.text = "Profundidad: " + perforacion.profundidadMetros.toString() + " mts"
        binding.numeroPerforacion.text = "Número de perforación: " + perforacion.numeroPerforacion
            .toString()
    }
}