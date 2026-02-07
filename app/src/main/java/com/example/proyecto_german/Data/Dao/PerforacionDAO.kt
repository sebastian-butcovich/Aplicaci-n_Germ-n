package com.example.proyecto_german.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.proyecto_german.Model.PerforacionConProfundidadesModel
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad

@Dao
interface PerforacionDAO {
    @Query("SELECT * FROM perforaciones")
    suspend fun getAllPerforaciones(): List<PerforacionModel>

    @Insert
    suspend fun agregarPerforacion(perforacionModel: PerforacionModel): Long
    @Insert
    suspend fun agregarProfundidades(profundidades:List<Profundidad>)
    @Transaction
    @Query("SELECT * FROM perforaciones")
    suspend fun getPerforacionConProfundiades(): List<PerforacionConProfundidadesModel>

    @Transaction
   suspend fun insetarPerforacionConProfundidades(
       perforacion: PerforacionModel,
       profundidades:List<Profundidad>
   )
   {
       //Agrego la perforacion
       val perforacionId = agregarPerforacion(perforacion)
       // Tomo cada profundidad
       //Esto tiene que devolver otra lista
       val profundiadesConId = profundidades.map {
           // A cada profundidad le agrego el Id de la perforación en su campo foreignt key
           it.copy(perforacionId = perforacionId)
       }
       //Agrego todas las profundiades
       agregarProfundidades(profundiadesConId)
   }
}