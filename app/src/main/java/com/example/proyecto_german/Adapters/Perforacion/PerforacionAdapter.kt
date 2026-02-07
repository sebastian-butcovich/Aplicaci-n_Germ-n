package com.example.proyecto_german.Adapters.Perforacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.R

class PerforacionAdapter(var perforaciones:List<PerforacionModel>,private val onClickListener: (PerforacionModel)-> Unit):
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
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int = perforaciones.size
    fun actualizarListaPerforacion(nuevaLista:List<PerforacionModel>){
        perforaciones =nuevaLista
        notifyDataSetChanged()
    }
}