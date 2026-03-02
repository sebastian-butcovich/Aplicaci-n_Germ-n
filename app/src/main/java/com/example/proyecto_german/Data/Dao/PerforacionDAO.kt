package com.example.proyecto_german.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.PerforacionConProfundidadesModel
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes

@Dao
interface PerforacionDAO {
    @Query("SELECT * FROM perforaciones")
    suspend fun getAllPerforaciones(): List<PerforacionModel>
    @Query("SELECT * FROM profundidades where perforacionId=:idPerforacion ")
    suspend fun obtenerProfundidadesDeUnaPerforacion( idPerforacion:Long):List<Profundidad>
    @Insert
    suspend fun agregarPerforacion(perforacionModel: PerforacionModel): Long
    @Insert
    suspend fun agregarProfundidad(profundidades:Profundidad) :Long
    @Insert
    suspend fun agregarGolpes(golpesStp: List<GolpesStp>)
    @Transaction
    @Query("SELECT * FROM perforaciones")
    suspend fun getPerforacionConProfundiades(): List<PerforacionConProfundidadesModel>
    @Query("SELECT * FROM golpesStp where profundidadId=:idProfundidad")
    suspend fun obtenerGolpesDeUnaProfundidad(idProfundidad:Long):List<GolpesStp>
    @Transaction
   suspend fun insertarPerforacionCompleta(
       perforacion: PerforacionModel,
       profundidadConGolpes: List<ProfundidadConGolpes>
   )
   {
       //Agrego la perforacion
       val perforacionId = agregarPerforacion(perforacion)
       // Tomo cada profundidad
       for(item in profundidadConGolpes){
           val profundidadId = agregarProfundidad(
               item.profundidad.copy(perforacionId=perforacionId)
           )
           //3. Convertir golpes temporales a entidad real
           val golpesReales = item.golpes.map {
               GolpesStp(
                   profundidadId=profundidadId,
                   profundidad_inicial = it.profundidad_inicial,
                   profundidad_final = it.profundidad_final,
                   numero_muestra = it.numero_muestra,
                   tipo=it.tipo,
                   golpes1 = it.golpes1,
                   golpes2 = it.golpes2,
                   golpes3 = it.golpes3
               )
           }
           agregarGolpes(golpesReales)
       }
   }
    @Query("DELETE FROM golpesStp WHERE id=:id")
    suspend fun eliminarGolpe(id: Long)
    @Query("DELETE FROM Profundidades WHERE id=:id")
    suspend fun  eliminarProfundidad(id:Long)
    @Query("DELETE FROM perforaciones where id=:id")
    suspend fun eliminarPerforacion(id:Long)

}