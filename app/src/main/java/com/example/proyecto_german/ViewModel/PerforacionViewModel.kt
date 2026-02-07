package com.example.proyecto_german.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.proyecto_german.Data.Dao.PerforacionDAO
import com.example.proyecto_german.Data.Database.PerforacionesDatabase
import com.example.proyecto_german.Model.PerforacionModel

import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Repository.PerforacionRepository
import kotlinx.coroutines.launch

class PerforacionViewModel(
    private val repository: PerforacionRepository
): ViewModel() {
    private val listProfundidades = MutableLiveData<List<Profundidad>>(emptyList())
    private val _perforacion = MutableLiveData<PerforacionModel?>(null)
    //Esta estructura va a guardar la lista de perforaciones.
    private val listPerforaciones = MutableLiveData<List<PerforacionModel>>(emptyList())
    var profundidades: LiveData<List<Profundidad>> = listProfundidades
    fun obtenerPerforaciones(){
        viewModelScope.launch {
            listPerforaciones.value = repository.obtenerPerforaciones()
        }
    }
    fun updateListProfundidades(profundidad: Profundidad){
        val listaActual = profundidades.value.orEmpty()
        listProfundidades.value = listaActual + profundidad
    }
    fun actulizarPerforacion(perforacion: PerforacionModel){
        _perforacion.value = perforacion;
    }
     fun agregarPerforacion(){
         val perforacion = _perforacion.value ?: return
         val profundidades = profundidades.value.orEmpty()
        viewModelScope.launch {
            repository.guardarPerforacionConProfundiades(perforacion,profundidades)
            _perforacion.postValue(null)
        }
    }
}