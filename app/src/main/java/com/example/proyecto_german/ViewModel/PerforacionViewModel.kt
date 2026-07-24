package com.example.proyecto_german.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.Perforacion
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes
import com.example.proyecto_german.Repository.PerforacionRepository
import kotlinx.coroutines.launch

class PerforacionViewModel(
    private val repository: PerforacionRepository
) : ViewModel() {
    // Está variable sirve para guardar la perforación nueva agregada y guardarla cuando sea necesario
    private val _perforacion = MutableLiveData<Perforacion?>()
    var perforacionEdit: Perforacion? = null

    //Esta estructura va a guardar la lista de perforaciones.
    private val _perforaciones = MutableLiveData<List<Perforacion>>()

    // Esta lista sirve para mostrar y dar seguimiento a los datos
    val perforaciones: LiveData<List<Perforacion>> = _perforaciones

    //Profundidades y golpes
    private val _profundidadGolpes = MutableLiveData<List<ProfundidadConGolpes>>(emptyList())
    val profundidadesConGolpes: LiveData<List<ProfundidadConGolpes>> get() = _profundidadGolpes
    var profundidadActual: Profundidad? = null
    val golpesActuales = mutableListOf<GolpesStp>()
    private val _golpesLiveData = MutableLiveData<List<GolpesStp>>(emptyList<GolpesStp>())
    val golpesLiveData: LiveData<List<GolpesStp>> get() = _golpesLiveData
    var _golpeActual = MutableLiveData<GolpesStp?>()
    val golpeActualLiveData: LiveData<GolpesStp?> = _golpeActual

    enum class ModoProfundidad {
        CREAR,
        EDITAR,
        VER
    }

    var modoProfundidad = ModoProfundidad.CREAR
    fun obtenerPerforaciones() {
        viewModelScope.launch {
            _perforaciones.value = repository.obtenerPerforaciones()
        }
    }

    fun agregarProfundidadConGolpes(
        profundidad: Profundidad,
        golpes: List<GolpesStp>
    ) {
        var lista  = profundidadesConGolpes.value.orEmpty() + ProfundidadConGolpes(profundidad, golpes)
        _profundidadGolpes.value = lista
    }

    fun actulizarPerforacion(perforacion: Perforacion) {
        _perforacion.value = perforacion;
    }

    fun agregarPerforacion() {
        val perforacion = _perforacion.value ?: return
        val profundidades = profundidadesConGolpes.value.orEmpty()
        viewModelScope.launch {
            if (perforacion.id == 0L) {
                repository.guardarPerforacionConProfundiades(perforacion, profundidades)
            } else {
                repository.acutliarPerforacionConProfundidad(perforacion, profundidades)
            }
            _perforacion.postValue(null)
            _profundidadGolpes.postValue(mutableListOf())
            //con esta linea debería poder volver a cero las profundidades
            _golpesLiveData.postValue(mutableListOf())
        }
    }

    fun agregarGolpe(golpe: GolpesStp) {
        golpesActuales.add(golpe)
        _golpesLiveData.value = golpesActuales.toList()
    }

    fun confirmarProfundidadConGolpes() {
        val profundidad = profundidadActual ?: return
        if(modoProfundidad == ModoProfundidad.CREAR){
            agregarProfundidadConGolpes(profundidad, golpesActuales.toList())
        }
        profundidadActual = null;
        golpesActuales.clear()
        _golpesLiveData.value = listOf()
    }

    suspend fun obtenerProfundidadesYGolpesDeUnaPerforacion(idPerforacion: Long): List<ProfundidadConGolpes> {
        val profundidades = obtenerProfundidadesDeUnaPerforacion(idPerforacion)
        var profundidadesConGolpes: MutableList<ProfundidadConGolpes> =
            mutableListOf<ProfundidadConGolpes>()
        for (profundidad in profundidades) {
            val listaGolpes = obtenerGolpesDeUnaProfundidad(profundidad.id)
            profundidadesConGolpes.add(
                ProfundidadConGolpes(profundidad, listaGolpes)
            )
        }
        return profundidadesConGolpes
    }

    suspend fun obtenerProfundidadesDeUnaPerforacion(idPerforacion: Long): List<Profundidad> {
        return repository.obtenerProfundiadesDeUnaPerforacion(idPerforacion)
    }

    suspend fun obtenerGolpesDeUnaProfundidad(idProfundidad: Long): List<GolpesStp> {
        return repository.obtenerGolpesDeUnaProfundidad(idProfundidad)
    }

    fun abrirPerforacionParaVisualizar(perforacion: Perforacion) {
        viewModelScope.launch {
            _perforacion.value = perforacion
            val lista = obtenerProfundidadesYGolpesDeUnaPerforacion(perforacion.id)
            _profundidadGolpes.value = lista.toMutableList()
        }
    }

    fun abrirGolpesParaVisualizar(profundidad: Profundidad) {
        profundidadActual = profundidad

        if (profundidad.id == 0L) {

            val lista = _profundidadGolpes.value
                ?.firstOrNull { it.profundidad == profundidad }
                ?.golpes
                ?: emptyList()

            golpesActuales.clear()
            golpesActuales.addAll(lista)

            _golpesLiveData.value = lista.toMutableList()

        } else {

            viewModelScope.launch {

                val golpes = obtenerGolpesDeUnaProfundidad(profundidad.id)

                golpesActuales.clear()
                golpesActuales.addAll(golpes)

                _golpesLiveData.postValue( golpes)
            }
        }
    }

    fun limpiarAgregarProfundidad() {
        modoProfundidad = PerforacionViewModel.ModoProfundidad.CREAR
        profundidadActual = null
        golpesActuales.clear()
        _golpesLiveData.value = emptyList<GolpesStp>()
    }

    fun eliminarGolpe(golpe: GolpesStp) {
        viewModelScope.launch {
            val profundidad = profundidadActual ?:return@launch
            if(golpe.id !=0L){

                repository.eliminarGolpe(golpe.id)
            }
            //eliminar golpes actuales
            golpesActuales.remove(golpe)
            //eliminar de la lista de profundidades con golpe
            val lista = _profundidadGolpes.value?.toMutableList()?:return@launch
            val index = lista.indexOfFirst { it.profundidad == profundidad }
            if(index !=-1){
                val profundidadConGolpes = lista[index]
                val nuevosGolpes = profundidadConGolpes.golpes
                    .filter { it !=golpe }
                lista[index] = profundidadConGolpes.copy(
                    golpes = nuevosGolpes.toMutableList()
                )
                _profundidadGolpes.value = lista
            }
            _golpesLiveData.value = golpesActuales.toMutableList()
            //abrirGolpesParaVisualizar(profundidad)
        }
    }

    fun eliminarProfundidad(profundidad: Profundidad) {
        viewModelScope.launch {
            if (profundidad.id != 0L) {
                repository.eliminarProfundidad(profundidad.id)
                val perforacion = _perforacion.value ?: return@launch
                val nuevaLista = obtenerProfundidadesYGolpesDeUnaPerforacion(perforacion.id)
                _profundidadGolpes.value = nuevaLista.toMutableList()
            } else {
                eliminarProfundiadDeLista(profundidad)
            }
        }
    }

    private fun eliminarProfundiadDeLista(profundidad: Profundidad) {
        val listaActual = _profundidadGolpes.value?.toMutableList() ?: return
        listaActual.removeAll { profundidadConGolpe ->
            profundidad.sucs == profundidadConGolpe.profundidad.sucs &&
                    profundidad.profundidadInicial == profundidadConGolpe.profundidad.profundidadInicial &&
                    profundidad.profundidadFinal == profundidadConGolpe.profundidad.profundidadFinal &&
                    profundidad.descripcion == profundidadConGolpe.profundidad.descripcion &&
                    profundidad.simbolo == profundidadConGolpe.profundidad.simbolo
        }
        _profundidadGolpes.value = listaActual
    }

    fun eliminarPerforacion(perforacion: Perforacion) {
        viewModelScope.launch {
            repository.eliminarPerforacion(perforacion.id)
            obtenerPerforaciones()
        }
    }

    fun seleccionarGolpe(golpe: GolpesStp) {
        _golpeActual.value = golpe
    }

    fun actualizarGolpe(golpeEditado: GolpesStp) {

        viewModelScope.launch {
            if (golpeEditado.id != 0L) {
                repository.actualizarGolpe(golpeEditado)
            }
            //Actualizar lista local si existe
            val index = golpesActuales.indexOfFirst {
                it.id == golpeEditado.id
            }
            if (index != -1) {
                golpesActuales[index] = golpeEditado
                _golpesLiveData.value = golpesActuales.toList()
            }
            //Actualizar profundidadGolpe si ya está cargada
            val lista = _profundidadGolpes.value?.toMutableList() ?: return@launch
            lista.forEachIndexed { i, profundidadConGolpes ->
                val golpes = profundidadConGolpes.golpes.toMutableList()
                val indexGolpes = golpes.indexOfFirst { it.id == golpeEditado.id }
                if (indexGolpes != -1) {
                    golpes[indexGolpes] = golpeEditado
                    lista[i] = profundidadConGolpes.copy(golpes = golpes)
                }
            }
            _profundidadGolpes.value = lista
        }
        _golpeActual.value = null
    }

    fun actuarlizarProfundidadBaseDatos(profundidad: Profundidad, golpes: List<GolpesStp>) {
        viewModelScope.launch {
            val golpesAgregar = golpes.filter {
                it.id == 0L
            }.map { golpe ->
                golpe.copy()

            }
            //Actualizar profundidad incial y final
            repository.agregarGolpe(golpesAgregar)
            repository.actualizarProfundidad(profundidad)
            profundidadActual = profundidad
            val perforacion = _perforacion.value ?: return@launch
            val nuevaLista = obtenerProfundidadesYGolpesDeUnaPerforacion(perforacion.id)
            _profundidadGolpes.value = nuevaLista.toMutableList()
            profundidadActual = null
        }
    }

    fun actualizarPerforacion(perforacion: Perforacion) {
        viewModelScope.launch {
            repository.actualizarPerforacion(perforacion)
        }

    }
     fun actualizarProfundidadEnMemoria(profundidad: Profundidad) {
       // 1. Obtener la lista actual
        val listaActual = _profundidadGolpes.value?.toMutableList() ?:return
         // 2. Mapeo la lista: creo una nueva lista cambiando solamente el elemento que coincide
         val listaActualizada = listaActual.map { item->
             if(item.profundidad.id == profundidad.id){
                 //Encontramos el elemento: lo clonamos con la nueva profundidad
                 item.copy(profundidad = profundidad,golpes = golpesActuales.toList())
             }else{
                 // No es el elemento que busco: lo dejo como estaba
                 item
             }
         }
            profundidadActual = profundidad;
             _profundidadGolpes.value = listaActualizada
         }
    fun limpiarProfundidades(){
        _profundidadGolpes.value = emptyList()
    }
}