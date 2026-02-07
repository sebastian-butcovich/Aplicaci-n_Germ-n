package com.example.proyecto_german.Adapters.Perforacion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.databinding.ItemPerforacionBinding

class PerforacionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemPerforacionBinding.bind(view)
    fun render(perforacion: PerforacionModel,onClickListener:(PerforacionModel)-> Unit){

    }
}