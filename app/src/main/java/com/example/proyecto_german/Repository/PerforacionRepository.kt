package com.example.proyecto_german.Repository

import android.util.Log
import com.example.proyecto_german.Data.Dao.PerforacionDAO
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes

class PerforacionRepository(private val dao: PerforacionDAO) {
    suspend fun guardarPerforacion(perforacion: PerforacionModel){
        val perforacionId = dao.agregarPerforacion(perforacion)
    }
    suspend fun guardarPerforacionConProfundiades(
        perforacion: PerforacionModel,
        profundiadConGolpes:List<ProfundidadConGolpes>
    ){
        dao.insertarPerforacionCompleta(perforacion,profundiadConGolpes)
    }
    suspend fun  obtenerPerforaciones():List<PerforacionModel>{
        return dao.getAllPerforaciones()
    }
    suspend fun obtenerProfundiadesDeUnaPerforacion(idPerforacion:Long):List<Profundidad>{
        return dao.obtenerProfundidadesDeUnaPerforacion(idPerforacion)
    }
    suspend fun obtenerGolpesDeUnaProfundidad(idProfundidad:Long):List<GolpesStp>{
        return dao.obtenerGolpesDeUnaProfundidad(idProfundidad)
    }
    suspend fun eliminarGolpe(id: Long){
        return dao.eliminarGolpe(id)
    }
    suspend fun eliminarProfundidad(id:Long){
        return dao.eliminarProfundidad(id)
    }
    suspend fun eliminarPerforacion(id:Long){
        return dao.eliminarPerforacion(id)
    }
    suspend fun actualizarGolpe(golpe: GolpesStp){
        return dao.actualizarGolpe(golpe)
    }
    suspend fun actualizarProfundidad(profundidad: Profundidad){
        return dao.actualizarProfundidad(profundidad)
    }
    suspend fun actualizarPerforacion(perforacion: PerforacionModel){
        return dao.actualizarPerforacion(perforacion)
    }
    suspend fun acutliarPerforacionConProfundidad(perforacion: PerforacionModel, profundidadesConGolpes:List<ProfundidadConGolpes>){
        return dao.actualizarPerforaciónCompleta(perforacion,profundidadesConGolpes)
    }
    //Esta función sirve para cuando actulizo una profundidad y al hacer agrego un golpe nuevo
    suspend fun agregarGolpe(golpes: List<GolpesStp>){
        return dao.agregarGolpes(golpes)
    }
}