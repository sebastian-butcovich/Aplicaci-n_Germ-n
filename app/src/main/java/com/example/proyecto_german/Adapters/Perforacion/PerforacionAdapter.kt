package com.example.proyecto_german.Adapters.Perforacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.Perforacion
import com.example.proyecto_german.R

class PerforacionAdapter(var perforaciones:List<Perforacion>,
                         private val onClickListener: (Perforacion)-> Unit,
                         private val onExportClick:(Perforacion)-> Unit,
                         private val onVerProfundidadesClick:(Perforacion)-> Unit,
                         private val onEliminarClick:(Perforacion)-> Unit,
                         private val onEditarClick:(Perforacion)-> Unit):
    RecyclerView.Adapter<PerforacionViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PerforacionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PerforacionViewHolder(
            layoutInflater.inflate(
                R.layout.item_perforacion,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PerforacionViewHolder,
        position: Int
    ) {
        val item = perforaciones.get(position)
        holder.render(item,onClickListener,onExportClick,onVerProfundidadesClick
        ,onEliminarClick,onEditarClick)
    }

    override fun getItemCount(): Int = perforaciones.size
    fun actualizarListaPerforacion(nuevaLista:List<Perforacion>){
        perforaciones =nuevaLista
        notifyDataSetChanged()
    }
}