package com.example.proyecto_german.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.OnClickListener
import com.example.proyecto_german.R

class ProfundiadAdapter(val profundidades: List<Profundidad>,private val onClickListener: (Profundidad)-> Unit): RecyclerView.Adapter<ProfundidadViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfundidadViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProfundidadViewHolder(layoutInflater.inflate(R.layout.item_profundidad,parent,false))
    }

    override fun onBindViewHolder(
        holder: ProfundidadViewHolder,
        position: Int
    ) {
        val item = profundidades.get(position)
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int = profundidades.size
}