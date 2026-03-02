package com.example.proyecto_german.Adapters.STP

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.databinding.ItemStpBinding

class StpViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemStpBinding.bind(view)
    fun render(golpesStp: GolpesStp,onClickListener:(GolpesStp)-> Unit,
               editarGolpe:(GolpesStp)-> Unit,
               eliminarGolpe:(GolpesStp)->Unit){
        binding.profundiadInicial.text = buildString {
            append("Profundidad Inicial: ")
            append(golpesStp.profundidad_inicial.toString())
        }
        binding.profundiadFinal.text = buildString{
            append("Profundidad Final: ")
            append(golpesStp.profundidad_final.toString())
        }
        binding.numeroMuestra.text = buildString{
            append("Número de muestra: ")
            append(golpesStp.numero_muestra.toString())
        }
        binding.tipo.text =buildString{
            append("Tipo: ")
            append(golpesStp.tipo)
        }
        binding.avanceStp1.text = buildString{
            append("Golpe 1: ")
            append(golpesStp.golpes1.toString())
        }
        binding.avanceStp2.text = buildString{
            append("Golpe 2: ")
            append(golpesStp.golpes2.toString())
        }
        binding.avanceStp3.text = buildString{
            append("Golpe: ")
            append(golpesStp.golpes3.toString())
        }
        itemView.setOnClickListener {
            onClickListener(golpesStp)
        }
        binding.botonEliminarGolpe.setOnClickListener {
            eliminarGolpe(golpesStp)
        }
    }
}