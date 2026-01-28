package com.example.proyecto_german.Data.Dao

//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
import com.example.proyecto_german.Model.PerforacionModel

//@Dao
interface PerforacionDAO {
  //  @Query("SELECT * FROM Perforacion")
    suspend fun getAllPerforaciones(): MutableList<PerforacionModel>
    //@Insert
    fun agregarPerforacion(perforacionModel: PerforacionModel): Long
}