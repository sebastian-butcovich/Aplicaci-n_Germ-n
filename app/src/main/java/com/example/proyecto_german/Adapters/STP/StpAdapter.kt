package com.example.proyecto_german.Adapters.STP

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.R

class StpAdapter(var golpes:List<GolpesStp>, private val onClickListener: (GolpesStp)-> Unit,
    private val editarGolpe:(GolpesStp)-> Unit,
    private val eliminarGolpe:(GolpesStp)-> Unit): RecyclerView.Adapter<StpViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StpViewHolder(
            layoutInflater.inflate(
                R.layout.item_stp,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: StpViewHolder,
        position: Int
    ) {
        val item = golpes.get(position)
        holder.render(item,onClickListener,editarGolpe,eliminarGolpe)
    }
     fun actualizarLista(listaGolpesStp:List<GolpesStp>){
        golpes = listaGolpesStp.toMutableList()
         notifyDataSetChanged()
    }
    override fun getItemCount(): Int = golpes.size
}