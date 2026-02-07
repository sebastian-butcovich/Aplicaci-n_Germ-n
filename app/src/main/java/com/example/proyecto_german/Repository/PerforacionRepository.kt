package com.example.proyecto_german.Repository

import android.util.Log
import com.example.proyecto_german.Data.Dao.PerforacionDAO
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad

class PerforacionRepository(private val dao: PerforacionDAO) {
    suspend fun guardarPerforacion(perforacion: PerforacionModel){
        val perforacionId = dao.agregarPerforacion(perforacion)
    }
    suspend fun guardarPerforacionConProfundiades(
        perforacion: PerforacionModel,
        profundiades:List<Profundidad>
    ){
        dao.insetarPerforacionConProfundidades(perforacion,profundiades)
    }
    suspend fun  obtenerPerforaciones():List<PerforacionModel>{
        return dao.getAllPerforaciones()
    }
}