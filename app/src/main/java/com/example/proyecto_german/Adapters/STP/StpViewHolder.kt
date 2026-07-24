package com.example.proyecto_german.Adapters.STP

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.databinding.ItemStpBinding

class StpViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemStpBinding.bind(view)

    fun render(golpesStp: GolpesStp,onClickListener:(GolpesStp)-> Unit,
               editarGolpe:(GolpesStp)-> Unit,
               eliminarGolpe:(GolpesStp)->Unit,
               soloLectura:Boolean){
        binding.profundiadInicial.text = buildString {
            append(golpesStp.profundidad_inicial.toString())
            append(" mts")
        }
        binding.profundiadFinal.text = buildString{
            append(golpesStp.profundidad_final.toString())
            append(" mts")
        }
        binding.numeroMuestra.text = buildString{

            append(golpesStp.numero_muestra.toString())
        }
        binding.tipo.text =buildString{
            append("Tipo: ")
            append(golpesStp.tipo)
        }
        binding.avanceStp1.text = buildString{
            append("STP 1: ")
            append(golpesStp.golpes1.toString())
        }
        binding.avanceStp2.text = buildString{
            append("STP 2: ")
            append(golpesStp.golpes2.toString())
        }
        binding.avanceStp3.text = buildString{
            append("STP 3: ")
            append(golpesStp.golpes3.toString())
        }
        itemView.setOnClickListener {
            onClickListener(golpesStp)
        }
        binding.botonEliminarGolpe.setOnClickListener {
            eliminarGolpe(golpesStp)
        }
        binding.botonEditarStp.setOnClickListener {
            editarGolpe(golpesStp)
        }
        if(soloLectura){
            binding.botonEliminarGolpe.visibility = View.GONE
            binding.botonEditarStp.visibility = View.GONE
        }else{
            binding.botonEliminarGolpe.visibility = View.VISIBLE
            binding.botonEditarStp.visibility = View.VISIBLE
        }

    }
}