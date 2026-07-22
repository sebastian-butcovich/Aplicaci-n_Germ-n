package com.example.proyecto_german.Adapters.Perforacion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.Perforacion
import com.example.proyecto_german.databinding.ItemPerforacionBinding

class PerforacionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemPerforacionBinding.bind(view)
    fun render(perforacion: Perforacion, onClickListener:(Perforacion)-> Unit,
               onExportClick:(Perforacion)-> Unit,
               onVerProfundidadesClick:(Perforacion)-> Unit,
               onEliminarClick:(Perforacion)-> Unit,
               onEditarClick:(Perforacion)-> Unit){
        binding.proyectoValue.text = perforacion.proyecto
        binding.valorCliente.text = perforacion.cliente
        binding.valorAtencion.text =  perforacion.atencion
        binding.valorProfundiad.text = perforacion.profundidadMetros.toString() + " mts"
        binding.valorNumeroPerforacion.text = perforacion.numeroPerforacion.toString()
        itemView.setOnClickListener {
            onClickListener(perforacion)
        }
        binding.btnExportar.setOnClickListener {
            onExportClick(perforacion)
        }
        binding.btnVerProfundidades.setOnClickListener {
            onVerProfundidadesClick(perforacion)
        }
        binding.btnEliminarProfundidad.setOnClickListener {
            onEliminarClick(perforacion)
        }
        binding.btnEditarProfundidad.setOnClickListener {
            onEditarClick(perforacion)
        }
    }
}