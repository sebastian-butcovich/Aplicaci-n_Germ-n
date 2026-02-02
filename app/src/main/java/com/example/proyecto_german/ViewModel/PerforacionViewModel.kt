package com.example.proyecto_german.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.proyecto_german.Model.Profundidad

class PerforacionViewModel: ViewModel() {
    private val listProfundidades = MutableLiveData<List<Profundidad>>(emptyList())
    var profundidades: LiveData<List<Profundidad>> = listProfundidades
    fun updateListProfundidades(profundidad: Profundidad){
        val listaActual = profundidades.value.orEmpty()
        listProfundidades.value = listaActual + profundidad
    }
}