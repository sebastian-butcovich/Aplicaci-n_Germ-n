package com.example.proyecto_german.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.PerforacionModel

import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes
import com.example.proyecto_german.Repository.PerforacionRepository
import kotlinx.coroutines.launch

class PerforacionViewModel(
    private val repository: PerforacionRepository
): ViewModel() {
    // Está variable sirve para guardar la perforación nueva agregada y guardarla cuando sea necesario
    private val _perforacion = MutableLiveData<PerforacionModel?>()
    //Esta estructura va a guardar la lista de perforaciones.
    private val _perforaciones = MutableLiveData<List<PerforacionModel>>()
    // Esta lista sirve para mostrar y dar seguimiento a los datos
    val perforaciones: LiveData<List<PerforacionModel>> = _perforaciones
    //Profundidades y golpes
    private val _profundidadGolpes = MutableLiveData<MutableList<ProfundidadConGolpes>>()
    val profundidadesConGolpes: LiveData<MutableList<ProfundidadConGolpes>>  get() = _profundidadGolpes
     var profundidadActual: Profundidad? =null
     val golpesActuales = mutableListOf<GolpesStp>()
     private val _golpesLiveData = MutableLiveData<List<GolpesStp>>(emptyList<GolpesStp>())
    val golpesLiveData: LiveData<List<GolpesStp>> get() = _golpesLiveData
    fun obtenerPerforaciones(){
        viewModelScope.launch {
            _perforaciones.value = repository.obtenerPerforaciones()
        }
    }

    fun agregarProfundidadConGolpes(
        profundidad: Profundidad,
        golpes: List<GolpesStp>
    ){
        val lista =profundidadesConGolpes.value?:mutableListOf()
        lista.add(ProfundidadConGolpes(profundidad,golpes))
        _profundidadGolpes.value = lista
    }

    fun actulizarPerforacion(perforacion: PerforacionModel){
        _perforacion.value = perforacion;
    }
     fun agregarPerforacion(){
         val perforacion = _perforacion.value ?: return
         val profundidades = profundidadesConGolpes.value.orEmpty()
        viewModelScope.launch {
            repository.guardarPerforacionConProfundiades(perforacion,profundidades)
            _perforacion.postValue(null)
            _profundidadGolpes.postValue(mutableListOf())
        }
    }
    fun actualizarProfundidad(profundidad: Profundidad){
        profundidadActual = profundidad
    }
    fun iniciarCargaDeGolpes(profundidad: Profundidad){
        profundidadActual = profundidad
        golpesActuales.clear()
    }
    fun agregarGolpe(golpe: GolpesStp){
        golpesActuales.add(golpe)
        _golpesLiveData.value = golpesActuales.toList()
    }
    fun confirmarProfundidadConGolpes(){
        val profundidad = profundidadActual ?: return
        agregarProfundidadConGolpes(profundidad,golpesActuales.toList())
        profundidadActual = null;
        golpesActuales.clear()
        _golpesLiveData.value = listOf()
    }
    suspend fun obtenerProfundidadesYGolpesDeUnaPerforacion(idPerforacion: Long):List<ProfundidadConGolpes>{
        val profundidades = obtenerProfundidadesDeUnaPerforacion(idPerforacion)
        var profundidadesConGolpes: MutableList<ProfundidadConGolpes> = mutableListOf<ProfundidadConGolpes>()
        for(profundidad in profundidades){
            val listaGolpes = obtenerGolpesDeUnaProfundidad(profundidad.id)
            profundidadesConGolpes.add(
                ProfundidadConGolpes(profundidad,listaGolpes)
            )
        }
        return profundidadesConGolpes
    }
    suspend fun obtenerProfundidadesDeUnaPerforacion(idPerforacion:Long):List<Profundidad>{
            return repository.obtenerProfundiadesDeUnaPerforacion(idPerforacion)
    }
    suspend fun obtenerGolpesDeUnaProfundidad(idProfundidad: Long):List<GolpesStp>{
            return repository.obtenerGolpesDeUnaProfundidad(idProfundidad)
    }
    fun abrirPerforacionParaVisualizar(perforacion: PerforacionModel){
        viewModelScope.launch {
            _perforacion.value = perforacion
            val lista = obtenerProfundidadesYGolpesDeUnaPerforacion(perforacion.id)
            _profundidadGolpes.value = lista.toMutableList()
        }
    }
    fun abrirGolpesParaVisualizar(profundidad: Profundidad){
        viewModelScope.launch {
            profundidadActual = profundidad
            val lista = obtenerGolpesDeUnaProfundidad(profundidad.id)
            _golpesLiveData.value = lista.toMutableList()
        }
    }
}